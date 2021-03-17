package com.example.bettertrialbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.forum.ForumActivity;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.Trial;

import java.util.ArrayList;

public class ExperimentViewActivity extends AppCompatActivity {
    Boolean newExperiment;
    Boolean isOwner;
    String experimentId;
    String experimentType;
    ExperimentInfo experimentInfo;
    final String TAG = "ExperimentViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);

        // get current experiment information from intent
        isOwner = getIntent().getBooleanExtra("IsOwner", false);
        newExperiment = getIntent().getBooleanExtra("NewExperiment", false);
        experimentInfo = getIntent().getExtras().getParcelable("ExperimentInfo");

        experimentId = experimentInfo.getId();
        experimentType = experimentInfo.getTrialType();

        // Populates experiment page with relevant text
        TextView regionText = findViewById(R.id.region_text);
        TextView descriptionText = findViewById(R.id.description_text);
        regionText.setText("Region: "+experimentInfo.getRegion());
        descriptionText.setText("Description: "+experimentInfo.getDescription());

        // hides owner-function buttons if current user is not the owner
        Button unpublishButton = findViewById(R.id.unpublish_button);
        Button endButton = findViewById(R.id.end_button);
        if (!isOwner) {
            unpublishButton.setVisibility(View.INVISIBLE);
            endButton.setVisibility(View.INVISIBLE);

        } else {
            // if already unpublished, sets button to allow re-publishing
            if (experimentInfo.getStatus().equals("Unpublished")) {
                unpublishButton.setText("Publish");

            } else if (experimentInfo.getStatus().equals("Closed")) {
                endButton.setText("Closed");
                // if permanently closed
                // endButton.setEnabled(false);
            }
        }

        unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unpublishButton.getText().equals("Unpublish")) {
                    unpublishButton.setText("Publish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.setExperimentStatus(experimentId, "Unpublish");
                } else {
                    unpublishButton.setText("Unpublish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.setExperimentStatus(experimentId, "Active");
                }
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endButton.getText().equals("End")) {
                    endButton.setText("Restart");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.setExperimentStatus(experimentId, "Closed");
                } else {
                    endButton.setText("End");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.setExperimentStatus(experimentId, "Active");
                }
            }
        });

        // set up the list of trials
        ListView trialList = findViewById(R.id.trial_listView);
        ArrayList<Trial> trialDataList = new ArrayList<>();
        ArrayAdapter<Trial> trialAdapter = new CustomTrialList(this, trialDataList);
        trialList.setAdapter(trialAdapter);
        ExperimentDAL experimentDAL = new ExperimentDAL();

        // create a document snapshot listener in the dal to update the list of trials
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

    // when back button pressed
    @Override
    public void onBackPressed() {
        if (newExperiment) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        } else {
            finish();
        }
    }
}