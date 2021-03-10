package com.example.bettertrialbook.dal;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.models.ExperimentInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        // initialize db
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        collectionReference
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
        // initialize db
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        collectionReference
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
}
