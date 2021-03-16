package com.example.bettertrialbook.dal;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

public class ExperimentDAL {

    final String TAG = "ExperimentDAL";
    // initialize db
    FirebaseFirestore db;
    CollectionReference collRef;

    public ExperimentDAL() {
        db = FirebaseFirestore.getInstance();
        collRef = db.collection("Experiments");
    }

    /**
     * adds the experiment to the firestore database
     * @param experimentInfo
     *  the information regarding the experiment that needs to be added
     * @param onCreate
     *  a callback used after successfully adding the data to the database
     */
    public void addExperiment(ExperimentInfo experimentInfo, @Nullable Callback<ExperimentInfo> onCreate) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("Description", experimentInfo.getDescription());
        data.put("Owner", experimentInfo.getOwnerId());
        data.put("Region", experimentInfo.getRegion());
        data.put("MinTrials", experimentInfo.getMinTrials());
        data.put("Status", experimentInfo.getStatus());
        data.put("GeoLocationRequired", experimentInfo.getGeoLocationRequired());
        data.put("TrialType", experimentInfo.getTrialType());

        collRef
                .add(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be added" + e.toString());
                    }
                })
                .addOnSuccessListener(docRef -> {
                    if (onCreate != null) {
                        experimentInfo.setId(docRef.getId());
                        onCreate.execute(experimentInfo);
                    }
                });
    }

    /**
     * Sets the status of the experiment with id: experimentId to be 'Unpublished'
     * meaning it can only be viewed by the owner
     * @param experimentId
     *  the unique id of the experiment to be unpublished
     */
    public void unpublishExperiment(String experimentId) {
        collRef
                .document(experimentId)
                .update("Status", "Unpublished")
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been updated");
                    }
                });
    }

    /**
     * Sets the status of the experiment with id: experimentId to be 'Active'
     * meaning it can be viewed by all users
     * @param experimentId
     *  the unique id of the experiment to be published
     */
    public void publishExperiment(String experimentId) {
        collRef
                .document(experimentId)
                .update("Status", "Active")
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been updated");
                    }
                });
    }

    /**
     * Adds a trial to an array of trials for the currently selected experiment
     * @param experimentId
     *  the id of the currently selected experiment
     * @param trial
     *  the trial to be added
     */
    public void addTrial(String experimentId, Trial trial) {
        collRef
                .document(experimentId)
                .update("Trials", FieldValue.arrayUnion(trial))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been updated");
                    }
                });
    }

    /**
     * Sets a documentsnapshot listener to update the list of trials for an experiment in real time
     * @param experimentId
     *  the id of the currently selected experiment
     * @param trialDataList
     *  the list of trials that the array adapter is displaying
     * @param trialAdapter
     *  the adapter for the experiment view being displayed
     * @param experimentType
     *  the type of experiment currently selected
     */
    public void addTrialListener(String experimentId, ArrayList<Trial> trialDataList, ArrayAdapter<Trial> trialAdapter, String experimentType) {
        Log.d(TAG, experimentId);
        final DocumentReference docRef = collRef.document(experimentId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                trialDataList.clear();
                // handle each of the four kinds of experiments
                if (experimentType.equals(Extras.COUNT_TYPE)) {
                    if (value != null && value.exists()) {
                        // kinda jank, from what I can tell, a hashmap is returned so need to access values through their keys
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                CountTrial countTrial = new CountTrial(Integer.parseInt(String.valueOf(trial.get("count"))), String.valueOf(trial.get("trialID")));
                                // Log.d(TAG, String.valueOf(trial.get("trialID")));
                                // Log.d(TAG, String.valueOf(trial.get("count")));
                                trialDataList.add(countTrial);
                            }
                        }
                    }
                } else if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                    if (value != null && value.exists()) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                BinomialTrial binomialTrial = new BinomialTrial(Integer.parseInt(String.valueOf(trial.get("passCount"))),
                                        Integer.parseInt(String.valueOf(trial.get("failCount"))), String.valueOf(trial.get("trialID")));
                                trialDataList.add(binomialTrial);
                            }
                        }
                    }
                } else if (experimentType.equals(Extras.NONNEG_TYPE)) {
                    if (value != null && value.exists()) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                NonNegTrial nonNegTrial = new NonNegTrial(Integer.parseInt(String.valueOf(trial.get("count"))), String.valueOf(trial.get("trialID")));
                                trialDataList.add(nonNegTrial);
                            }
                        }
                    }
                } else {
                    if (value != null && value.exists()) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                MeasurementTrial measurementTrial = new MeasurementTrial(Double.parseDouble(String.valueOf(trial.get("measurement"))), String.valueOf(trial.get("trialID")));
                                trialDataList.add(measurementTrial);
                            }
                        }
                    }
                }
                trialAdapter.notifyDataSetChanged();
            }
        });
    }
}
