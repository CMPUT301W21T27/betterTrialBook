package com.example.bettertrialbook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.forum.ForumActivity;
import com.example.bettertrialbook.models.Experiment;
import com.example.bettertrialbook.models.Trial;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExperimentViewActivity extends AppCompatActivity {
    String experimentId;
    String experimentType;
    private ArrayAdapter<Trial> trialAdapter;
    private ArrayList<Trial> trialDataList;
    final String TAG = "ExperimentViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);

        // get id of currently selected experiment
        // experimentId = getIntent().getStringExtra(Extras.EXPERIMENT_ID);

        // testing id
        // X438IwaockIjHScMTYAG
        // FwrSePYufKWUSlVCQnL4
        experimentId = "FwrSePYufKWUSlVCQnL4";
        experimentType = "Measurement";

        Button unpublishButton = findViewById(R.id.unpublish_button);

        unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unpublishButton.getText().equals("Unpublish")) {
                    unpublishButton.setText("Publish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.unpublishExperiment(experimentId);
                } else {
                    unpublishButton.setText("Unpublish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.publishExperiment(experimentId);
                }
            }
        });

        ListView trialList = findViewById(R.id.trial_listView);
        trialDataList = new ArrayList<>();
        trialAdapter = new CustomTrialList(this, trialDataList);
        trialList.setAdapter(trialAdapter);
        ExperimentDAL experimentDAL = new ExperimentDAL();
        experimentDAL.addTrialListener(experimentId, trialDataList, trialAdapter, experimentType);

        Button addTrialButton = findViewById(R.id.addTrial_button);
        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddTrialDialogFragment.newInstance(experimentType, experimentId);
                newFragment.show(getSupportFragmentManager(), "ADD TRIAL");
            }
        });
    }

    public void openForum(View view) {
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra(Extras.EXPERIMENT_ID, experimentId);
        startActivity(intent);
    }
}