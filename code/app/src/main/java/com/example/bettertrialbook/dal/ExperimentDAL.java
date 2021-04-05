/*
The ExperimentDAL handles all communication with the database regarding experiments and trials.
Currently blacklisting has yet to be implemented.
 */

package com.example.bettertrialbook.dal;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
     * Deletes an experimenter's trials
     *
     * @param experimentId   the id of the currently selected experiment
     * @param experimentType the trial to be added//////////////////////////////////
     * @param experimenterId the blacklisted experimented
     * @param blacklist      the trial's blacklist
     *                       status////////////////////////////////////
     */
    public void modifyExperimentBlacklist(String experimentId, String experimentType, String experimenterId,
            Boolean blacklist) {
        collRef.document(experimentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot value) {
                if (value != null && value.exists()) {
                    ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData())
                            .get("Trials");
                    if (trials != null) {
                        // trials.removeIf(t -> t.get("experimenterID").equals(experimenterId));
                        ListIterator<HashMap<Object, Object>> iter = trials.listIterator();
                        while (iter.hasNext()) {
                            String temp = (String) iter.next().get("experimenterId");
                            Log.d("TEST", "blacklist: " + experimenterId);
                            if (temp != null) {
                                Log.d("TEST", "trial experimenter: " + temp);

                                if (temp.equals(experimenterId)) {
                                    iter.remove();
                                }
                            }
                        }
                        /*
                         * for (HashMap<Object, Object> trial : trials) { if
                         * (experimenterId.equals(trial.get("experimenterID"))) { trials.remove(trial);
                         * 
                         * } }
                         */
                    }
                }
                /*
                 * if (experimentType.equals(Extras.COUNT_TYPE)) { // kinda jank, from what I
                 * can tell, a hashmap is returned so need to access values through their keys
                 * 
                 * 
                 * } else if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                 * 
                 * ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object,
                 * Object>>) (value.getData()).get("Trials"); if (trials != null) { for
                 * (HashMap<Object, Object> trial : trials) { BinomialTrial binomialTrial = new
                 * BinomialTrial(Integer.parseInt(String.valueOf(trial.get("passCount"))),
                 * Integer.parseInt(String.valueOf(trial.get("failCount"))),
                 * String.valueOf(trial.get("trialID")),
                 * String.valueOf(trial.get("experimenterID"))); } }
                 * 
                 * } else if (experimentType.equals(Extras.NONNEG_TYPE)) {
                 * 
                 * ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object,
                 * Object>>) (value.getData()).get("Trials"); if (trials != null) { for
                 * (HashMap<Object, Object> trial : trials) { NonNegTrial nonNegTrial = new
                 * NonNegTrial(Integer.parseInt(String.valueOf(trial.get("count"))),
                 * String.valueOf(trial.get("trialID")),
                 * String.valueOf(trial.get("experimenterID"))); } }
                 * 
                 * } else {
                 * 
                 * ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object,
                 * Object>>) (value.getData()).get("Trials"); if (trials != null) { for
                 * (HashMap<Object, Object> trial : trials) { MeasurementTrial measurementTrial
                 * = new
                 * MeasurementTrial(Double.parseDouble(String.valueOf(trial.get("measurement")))
                 * , String.valueOf(trial.get("trialID")),
                 * String.valueOf(trial.get("experimenterID"))); } } }
                 */
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

            for (HashMap<Object, Object> trial : trials) {
                trialList.add(deserializeTrial(trial, experimentType));
            }
            callback.execute(trialList);
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
        // deserialize the geolocation within the trial
        Geolocation geolocation;
        Location newLocation = new Location("");
        if (data.get("geolocation") != null) {
            HashMap<Object, Object> geolocationData = (HashMap<Object, Object>) data.get("geolocation");
            HashMap<Object, Object> locationData = (HashMap<Object, Object>) geolocationData.get("location");
            newLocation.setLatitude(Double.parseDouble(String.valueOf(locationData.get("latitude"))));
            newLocation.setLongitude(Double.parseDouble(String.valueOf(locationData.get("longitude"))));
            geolocation = new Geolocation(newLocation);
        } else {
            // for trials with no geolocation, set the geolocation's location to null
            geolocation = new Geolocation(null);
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
            int count = ((Long) data.get("count")).intValue();
            return new CountTrial(count, trialId, experimenterId, geolocation);
        case Extras.BINOMIAL_TYPE:
            int passCount = ((Long) data.get("passCount")).intValue();
            int failCount = ((Long) data.get("failCount")).intValue();
            return new BinomialTrial(passCount, failCount, trialId, experimenterId, geolocation);
        case Extras.NONNEG_TYPE:
            count = ((Long) data.get("count")).intValue();
            return new NonNegTrial(count, trialId, experimenterId, geolocation);
        case Extras.MEASUREMENT_TYPE:
            double measurement = (double) data.get("measurement");
            return new MeasurementTrial(measurement, trialId, experimenterId, geolocation);
        default:
            throw new IllegalArgumentException("Invalid experiment type");
        }
    }


}
