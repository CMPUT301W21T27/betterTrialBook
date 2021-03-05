package com.example.bettertrialbook.dal;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bettertrialbook.Experiment;
import com.example.bettertrialbook.ExperimentInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ExperimentDAL {

    final String TAG = "ExperimentDAL";

    public void addExperiment(ExperimentInfo experimentInfo) {
        // initialize db
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        HashMap<String, Object> data = new HashMap<>();
        data.put("Description", experimentInfo.getDescription());
        data.put("Region", experimentInfo.getRegion());
        data.put("MinTrials", experimentInfo.getMinTrials());
        data.put("Status", experimentInfo.getStatus());
        data.put("GeoLocationRequired", experimentInfo.getGeoLocationRequired());
        data.put("TrialType", experimentInfo.getTrialType());

        collectionReference
                .document()
                .set(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be added" + e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been added");
                    }
                });

        Experiment experiment = new Experiment(experimentInfo);
    }
}
