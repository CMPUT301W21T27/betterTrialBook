/*
The ExperimentDAL handles all communication with the database regarding experiments and trials.
Currently blacklisting has yet to be implemented.
 */

package com.example.bettertrialbook.dal;

import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
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

    public interface IsBlacklistedCallback {
        void onCallback(Boolean isBlacklisted);
    }

    public interface IsOpenCallback {
        void onCallback(String status);
    }

    public interface GetExperimentsByStatusCallback {
        void onCallback(List<String> experiments);
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
     * Deletes an experiment from firebase
     *
     * @param experimentId - the id of the experiment
     */
    public void deleteExperiment(String experimentId) {
        collRef.document(experimentId).delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Experiment could not be deleted" + e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Experiment has been deleted");
            }
        });;
    }

    /**
     * Gets all experiments of a certain status
     *
     * @param status - the status of the desired experiments
     * @param callback - return method for firestore queries
     */
    public void getExperiments(String status, GetExperimentsByStatusCallback callback) {
        final List<String>[] experiments = new List[]{new ArrayList<String>()};
        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> allExperiments = task.getResult().getDocuments();
                    for (int i = 0; i < allExperiments.size(); i++) {
                        String publishStatus = (String) allExperiments.get(i).get("PublishStatus");
                        if (publishStatus != null && publishStatus.equals(status)) {
                            String experimentId = (String) allExperiments.get(i).getId();
                            experiments[0].add(experimentId);
                        }
                    }
                }
                callback.onCallback(experiments[0]);
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
                Log.d(TAG, "Status could not be updated" + e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Status has been updated");
            }
        });
    }

    /**
     * Update experiment description
     *
     * @param experimentId - the unique id of the experiment to be updated
     * @param description - updated description
     */
    public void setExperimentDescription(String experimentId, String description) {
        collRef.document(experimentId).update("Description", description).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Description could not be updated" + e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Description has been updated");
            }
        });
    }

    /**
     * Update experiment region
     *
     * @param experimentId - the unique id of the experiment to be updated
     * @param region - updated region
     */
    public void setExperimentRegion(String experimentId, String region) {
        collRef.document(experimentId).update("Region", region).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Region could not be updated" + e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Region has been updated");
            }
        });
    }

    /**
     * Adds listener to callback with new status of experiment
     *
     * @param experimentId   the id of the currently selected experiment
     */
    public void addStatusListener(String experimentId, IsOpenCallback callback) {
        final DocumentReference docRef = collRef.document(experimentId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String status = "";
                if (value != null && value.exists()) {
                    String activeStatus = (String) value.getData().get("ActiveStatus");
                    String publishStatus = (String) value.getData().get("PublishStatus");
                    if (activeStatus != null && activeStatus.equals("Closed")) {
                        status = "Closed";
                    }
                    if (publishStatus != null && publishStatus.equals("Unpublish")) {
                        status = "Unpublish";
                    }
                }
                callback.onCallback(status);
            }
        });
    }

    /**
     * Add new trial to experiment
     *
     * @param experimentId the unique id of the experiment
     * @param trial       trial and its data
     */
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
     * Sets a documentsnapshot listener to update the list of trials for an
     * experiment in real time
     *
     * @param experimentId   the id of the currently selected experiment
     * @param callback       Callback that will be executed with the
     */
    public void addTrialListener(String experimentId, Callback<List<Trial>> callback) {
        Log.d(TAG, experimentId);
        final DocumentReference docRef = collRef.document(experimentId);
        docRef.addSnapshotListener((value, error) -> {
            List<Trial> trialList = new ArrayList<>();
            if (value == null || !value.exists()) {
                callback.execute(trialList);
                return;
            }
            String trialType = (String) value.get("TrialType");
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
                    trialList.add(deserializeTrial(currentIter, trialType));
                }
            }

            callback.execute(trialList);
        });
    }

    /**
     * Adds or removes an experimenter + their trials from a blacklist
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimenterId the experimenter to be added or removed
     * @param blacklist      the trial/experimenter's blacklist status
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

    /**
     * Returns blacklist status of user
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimenterId the experimenter to be checked
     */
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

    /**
     * Adds listener to automatically update blacklist status of all trials when user blacklisted
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimenterId the experimenter to be added or removed
     */
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

    /**
     * Blacklists user and adds them to experiment's Blacklist array
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimenterId the experimenter to be added or removed
     */
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

    /**
     * Unblacklists user and removes them from experiment's Blacklist array
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimenterId the experimenter to be added or removed
     */
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
     * Searching the experiment using keywords matching the description
     * @param trialInfoList
     * An Array of ExperimentInfo represents the previous searched result
     * @param trialInfoAdapter
     * A Screen Adapter used for displaying the experiment
     * @param newText
     * The text that the user typed in the Search Bar in the Home Screen (Main Activity)
     */
    public void searchByDescription(ArrayList<ExperimentInfo> trialInfoList, ArrayAdapter<ExperimentInfo> trialInfoAdapter, String newText) {
        collRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
           trialInfoList.clear();
           if (newText.length() > 0) {
               for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                   // Only search for the key words in the description
                   // Further search method will be refined after
                   String description = (String) doc.getData().get("Description");
                   if (description.toLowerCase().contains(newText.toLowerCase())) {
                       // MinTrials hasn't done yet. Want to wait for further production and then
                       // decide.
                       // GeoLocationRequired hasn't done yet. Want to wait for further production and
                       // then decide.
                       String ownerId = (String) doc.getData().get("Owner");
                       String publishStatus = (String) doc.getData().get("PublishStatus");
                       String activeStatus = (String) doc.getData().get("ActiveStatus");
                       if ((ownerId != null && ownerId.equals(You.getUser().getID()))
                               || (publishStatus != null && !publishStatus.equals("Unpublish"))) {
                           String id = doc.getId();
                           String region = (String) doc.getData().get("Region");
                           String trialType = (String) doc.getData().get("TrialType");
                           trialInfoList.add(new ExperimentInfo(description, ownerId, publishStatus, activeStatus,
                                   id, trialType, false, 0, region));
                       }
                   }
               }
           } else {
               trialInfoList.clear();
           }
            trialInfoAdapter.notifyDataSetChanged();
        });
    }

    // This method has to be refined
    /*
     * Right now this method search the user based on their document name on the fireBase
     * It doesn't search based on the username in the User document as register is not part of the requirement
     */
    public void searchByUser(ArrayList<ExperimentInfo> trialInfoList, ArrayAdapter<ExperimentInfo> trialInfoAdapter, String newText) {
        collRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            trialInfoList.clear();
            if (newText.length() > 0) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    // Only search for the key words in the description
                    // Further search method will be refined after
                    String ownerId = (String) doc.getData().get("Owner");
                    if (ownerId.substring(0,7).toLowerCase().contains(newText.toLowerCase())) {
                        // MinTrials hasn't done yet. Want to wait for further production and then
                        // decide.
                        // GeoLocationRequired hasn't done yet. Want to wait for further production and
                        // then decide.
                        String description = (String) doc.getData().get("Description");
                        String publishStatus = (String) doc.getData().get("PublishStatus");
                        String activeStatus = (String) doc.getData().get("ActiveStatus");
                        if ((ownerId != null && ownerId.equals(You.getUser().getID()))
                                || (publishStatus != null && !publishStatus.equals("Unpublish"))) {
                            String id = doc.getId();
                            String region = (String) doc.getData().get("Region");
                            String trialType = (String) doc.getData().get("TrialType");
                            trialInfoList.add(new ExperimentInfo(description, ownerId, publishStatus, activeStatus,
                                    id, trialType, false, 0, region));
                        }
                    }
                }
            } else {
                trialInfoList.clear();
            }
            trialInfoAdapter.notifyDataSetChanged();
        });
    }

    private Trial deserializeTrial(HashMap<Object, Object> data, String experimentType) {
        String trialId = data.get("trialID").toString();
        String experimenterId;
        Date timestamp = null;
        if (data.get("timestamp") != null) {
            timestamp = ((Timestamp) data.get("timestamp")).toDate();
            Log.d("ExperimentDAL", String.valueOf(timestamp));
        }
        // deserialize the geolocation within the trial
        // for trials with no geolocation, set the geolocation's location to null
        Geolocation geolocation = new Geolocation(null);
        Location newLocation = new Location("");
        if (data.get("geolocation") != null) {
            HashMap<Object, Object> geolocationData = (HashMap<Object, Object>) data.get("geolocation");
            if (geolocationData.get("location") != null) {
                HashMap<Object, Object> locationData = (HashMap<Object, Object>) geolocationData.get("location");
                newLocation.setLatitude(Double.parseDouble(String.valueOf(locationData.get("latitude"))));
                newLocation.setLongitude(Double.parseDouble(String.valueOf(locationData.get("longitude"))));
                geolocation = new Geolocation(newLocation);
            }
        }
        // some trials in firestore don't have an experiment id, this is just so our app
        // doesn't crash
        if (data.get("experimenterID") == null) {
            Log.w("Trials", "Trial with id " + trialId + " has no experimenter id. Using default");
            experimenterId = "1234";
        } else
            experimenterId = data.get("experimenterID").toString();
        switch (experimentType) {
        case Extras.COUNT_TYPE:
            return new CountTrial(trialId, experimenterId, geolocation, timestamp);
        case Extras.BINOMIAL_TYPE:
            boolean success = (boolean) data.get("success");
            return new BinomialTrial(success, trialId, experimenterId, geolocation, timestamp);
        case Extras.NONNEG_TYPE:
            int count = ((Long) data.get("count")).intValue();
            return new NonNegTrial(count, trialId, experimenterId, geolocation, timestamp);
        case Extras.MEASUREMENT_TYPE:
            double measurement = (double) data.get("measurement");
            return new MeasurementTrial(measurement, trialId, experimenterId, geolocation, timestamp);
        default:
            throw new IllegalArgumentException("Invalid experiment type");
        }
    }
}
