package com.example.bettertrialbook.dal;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.CustomTrialList;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void addExperiment(ExperimentInfo experimentInfo, @Nullable Callback<String> onCreate) {

        HashMap<String, Object> data = new HashMap<>();
        data.put("Description", experimentInfo.getDescription());
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
                    if (onCreate != null)
                        onCreate.execute(docRef.getId());
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

    public void addTrialListener(String experimentId, ArrayList<Trial> trialDataList, ArrayAdapter<Trial> trialAdapter, String experimentType) {
        final DocumentReference docRef = collRef.document(experimentId);
        Log.d(TAG, experimentId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                trialDataList.clear();
                if (experimentType.equals("Count-Based")) {
                    if (value != null && value.exists()) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                CountTrial countTrial = new CountTrial();
                                countTrial.setTrialID(String.valueOf(trial.get("trialID")));
                                countTrial.setCount(Integer.parseInt(String.valueOf(trial.get("count"))));
                                Log.d(TAG, String.valueOf(trial.get("trialID")));
                                Log.d(TAG, String.valueOf(trial.get("count")));
                                trialDataList.add(countTrial);
                            }
                        }
                    }
                } else if (experimentType.equals("Binomial")) {
                    if (value != null && value.exists()) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                BinomialTrial binomialTrial = new BinomialTrial();
                                binomialTrial.setTrialID(String.valueOf(trial.get("trialID")));
                                binomialTrial.setFailCount(Integer.parseInt(String.valueOf(trial.get("failCount"))));
                                binomialTrial.setPassCount(Integer.parseInt(String.valueOf(trial.get("passCount"))));
                                trialDataList.add(binomialTrial);
                            }
                        }
                    }
                } else if (experimentType.equals("Non-Negative Integer")) {
                    if (value != null && value.exists()) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                NonNegTrial nonNegTrial = new NonNegTrial();
                                nonNegTrial.setTrialID(String.valueOf(trial.get("trialID")));
                                nonNegTrial.setCount(Integer.parseInt(String.valueOf(trial.get("count"))));
                                trialDataList.add(nonNegTrial);
                            }
                        }
                    }
                } else {
                    if (value != null && value.exists()) {
                        ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) (value.getData()).get("Trials");
                        if (trials != null) {
                            for (HashMap<Object, Object> trial : trials) {
                                MeasurementTrial measurementTrial = new MeasurementTrial();
                                measurementTrial.setTrialID(String.valueOf(trial.get("trialID")));
                                measurementTrial.setMeasurement(Double.parseDouble(String.valueOf(trial.get("measurement"))));
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
