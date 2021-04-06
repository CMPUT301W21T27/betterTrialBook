/*
The ExperimentDAL handles all communication with the database regarding experiments and trials.
Currently blacklisting has yet to be implemented.
 */

package com.example.bettertrialbook.dal;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Encapsulates access to Experiments and Trials in Firebase. Currently
 * blacklisting has yet to be implemented.
 */
public class ExperimentDAL {

    final String TAG = "ExperimentDAL";
    // initialize db
    FirebaseFirestore db;
    CollectionReference collRef;

    public ExperimentDAL() {
        db = Firestore.getInstance();
        collRef = db.collection("Experiments");
    }

    // Interfaces for callbacks
    public interface FindExperimentByIDCallback {
        void onCallback(ExperimentInfo experimentInfo);
    }

    /**
     * Searches database for given ID Returns user object if found, or null if not
     * found
     *
     * @param id       - Id to search for
     * @param callback - return method for firestore queries
     */
    public void findExperimentByID(String id, FindExperimentByIDCallback callback) {
        collRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ExperimentInfo experimentInfo;
                    if (document.exists()) {
                        // experiment found
                        String description = document.getString("Description");
                        String publishStatus = document.getString("PublishStatus");
                        String activeStatus = document.getString("ActiveStatus");
                        String ownerId = document.getString("Owner");
                        String trialType = document.getString("TrialType");
                        Boolean geoLocation = document.getBoolean("GeoLocationRequired");
                        int minTrials = document.getLong("MinTrials").intValue();
                        String region = document.getString("Region");
                        experimentInfo = new ExperimentInfo(description, ownerId, publishStatus, activeStatus, id,
                                trialType, geoLocation, minTrials, region);

                    } else {
                        // User not found, return null
                        Log.d("TEST", "Experiment does not exist");
                        experimentInfo = null;
                    }
                    callback.onCallback(experimentInfo);
                } else {
                    // Error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });
    }

    /**
     * adds the experiment to the firestore database
     *
     * @param experimentInfo the information regarding the experiment that needs to
     *                       be added
     * @param onCreate       a callback used after successfully adding the data to
     *                       the database
     */
    public void addExperiment(ExperimentInfo experimentInfo, @Nullable Callback<ExperimentInfo> onCreate) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("Description", experimentInfo.getDescription());
        data.put("Owner", experimentInfo.getOwnerId());
        data.put("Region", experimentInfo.getRegion());
        data.put("MinTrials", experimentInfo.getMinTrials());
        data.put("PublishStatus", experimentInfo.getPublishStatus());
        data.put("ActiveStatus", experimentInfo.getActiveStatus());
        data.put("GeoLocationRequired", experimentInfo.getGeoLocationRequired());
        data.put("TrialType", experimentInfo.getTrialType());

        collRef.add(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Data could not be added" + e.toString());
            }
        }).addOnSuccessListener(docRef -> {
            if (onCreate != null) {
                experimentInfo.setId(docRef.getId());
                onCreate.execute(experimentInfo);
            }
        });
    }

    /**
     * Update status of experiment
     *
     * @param experimentId the unique id of the experiment to be updated
     * @param status       the new status of the experiment
     */
    public void setExperimentStatus(String experimentId, String status, String newStatus) {
        collRef.document(experimentId).update(status, newStatus).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Data could not be updated" + e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data has been updated");
            }
        });
    }

    public void addTrial(String experimentId, Trial trial) {
        collRef.document(experimentId).update("Trials", FieldValue.arrayUnion(trial))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been updated");
                    }
                });
    }

    /**
     * Adds or removes an experimenter + their trials from a blacklist
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimenterId the experimenter to be added or removed
     * @param blacklist      the trial's blacklist
     */
    public void modifyBlacklist(String experimentId, String experimenterId, Boolean blacklist) {
        if (blacklist) {
            blacklistUser(experimentId, experimenterId);

        } else {
            unblacklistUser(experimentId, experimenterId);
        }
        collRef.document(experimentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (document.getData()).get("Trials");
                        if (trials != null) {
                            ListIterator<HashMap<Object, Object>> iter = trials.listIterator();
                            ArrayList<HashMap<Object, Object>> updatedTrials = new ArrayList<HashMap<Object, Object>>();
                            while (iter.hasNext()) {
                                HashMap<Object, Object> currentIter = iter.next();
                                String currentID = (String) currentIter.get("experimenterID");
                                if (currentID != null) {
                                    if (currentID.equals(experimenterId)) {
                                        currentIter.put("blacklist", blacklist);
                                    }
                                    updatedTrials.add(currentIter);
                                    Log.d("TEST", "blacklist: " + currentID + " " + String.valueOf(currentIter.get("blacklist")));
                                }
                            }
                            collRef.document(experimentId).update("Trials", updatedTrials);
                        }
                    }
                }
            }
        });
    }

    public interface IsBlacklistedCallback {
        void onCallback(Boolean isBlacklisted);
    }

    public void getBlacklistUser(String experimentId, String experimenterId, IsBlacklistedCallback callback) {
        collRef.document(experimentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                boolean isBlacklisted = false;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "Document has been loaded");
                        List<String> blacklist = (List<String>) document.get("Blacklist");
                        Log.d("TEST2", experimenterId);
                        if (blacklist != null) {
                            for (int i = 0; i < blacklist.size(); i++) {
                                String current = blacklist.get(i);
                                Log.d("TEST2", current);
                                if (current.equals(experimenterId)) {
                                    isBlacklisted = true;
                                    break;
                                }
                            }
                        }

                    } else {
                        Log.d(TAG, "No such document exists");
                    }

                } else {
                    Log.d(TAG, "Document could not be loaded");
                }
                callback.onCallback(isBlacklisted);
            }
        });
    }

    public void addBlacklistListener(String experimentId, String experimenterId, IsBlacklistedCallback callback) {
        final DocumentReference docRef = collRef.document(experimentId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                boolean isBlacklisted = false;
                if (value != null && value.exists()) {
                    List<String> blacklist = (List<String>) value.getData().get("Blacklist");
                    if (blacklist != null) {
                        for (int i = 0; i < blacklist.size(); i++) {
                            String current = blacklist.get(i);
                            if (current.equals(experimenterId)) {
                                isBlacklisted = true;
                                break;
                            }
                        }
                    }
                }
                callback.onCallback(isBlacklisted);
            }
        });
    }

    public void blacklistUser(String experimentId, String experimenterId) {
        collRef.document(experimentId).update("Blacklist", FieldValue.arrayUnion(experimenterId))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data has been updated");
            }
        });
    }

    public void unblacklistUser(String experimentId, String experimenterId) {
        collRef.document(experimentId).update("Blacklist", FieldValue.arrayRemove(experimenterId))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data has been updated");
            }
        });
    }

    /**
     * Sets a documentsnapshot listener to update the list of trials for an
     * experiment in real time
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimentType the type of experiment currently selected
     * @param callback       Callback that will be executed with the
     */
    public void addTrialListener(String experimentId, String experimentType, Callback<List<Trial>> callback) {
        Log.d(TAG, experimentId);
        final DocumentReference docRef = collRef.document(experimentId);
        docRef.addSnapshotListener((value, error) -> {
            List<Trial> trialList = new ArrayList<>();
            if (value == null || !value.exists()) {
                callback.execute(trialList);
                return;
            }
            ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData())
                    .get("Trials");
            if (trials == null) {
                callback.execute(trialList);
                return;
            }

            // only adds non-blacklisted trials
            ListIterator<HashMap<Object, Object>> iter = trials.listIterator();
            ArrayList<HashMap<Object, Object>> updatedTrials = new ArrayList<HashMap<Object, Object>>();
            while (iter.hasNext()) {
                HashMap<Object, Object> currentIter = iter.next();
                Boolean blacklist = (Boolean) currentIter.get("blacklist");
                if (blacklist != null && !blacklist) {
                    trialList.add(deserializeTrial(currentIter, experimentType));
                }
            }

            callback.execute(trialList);
        });
    }

    private Trial deserializeTrial(HashMap<Object, Object> data, String experimentType) {
        String trialId = data.get("trialID").toString();
        String experimenterId;
        // some trials in firestore don't have an experiment id, this is just so our app
        // doesn't crash
        if (data.get("experimenterID") == null) {
            Log.w("Trials", "Trial with id " + trialId + " has no experimenter id. Using default");
            experimenterId = "1234";
        } else
            experimenterId = data.get("experimenterID").toString();
        switch (experimentType) {
        case Extras.COUNT_TYPE:
            int count = ((Long) data.get("count")).intValue();
            return new CountTrial(count, trialId, experimenterId);
        case Extras.BINOMIAL_TYPE:
            int passCount = ((Long) data.get("passCount")).intValue();
            int failCount = ((Long) data.get("failCount")).intValue();
            return new BinomialTrial(passCount, failCount, trialId, experimenterId);
        case Extras.NONNEG_TYPE:
            count = ((Long) data.get("count")).intValue();
            return new NonNegTrial(count, trialId, experimenterId);
        case Extras.MEASUREMENT_TYPE:
            double measurement = (double) data.get("measurement");
            return new MeasurementTrial(measurement, trialId, experimenterId);
        default:
            throw new IllegalArgumentException("Invalid experiment type");
        }
    }
}
