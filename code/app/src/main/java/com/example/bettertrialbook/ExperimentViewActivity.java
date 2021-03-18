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
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.forum.ForumActivity;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.User;

import java.util.ArrayList;

public class ExperimentViewActivity extends AppCompatActivity implements ConfirmationFragment.OnFragmentInteractionListener {
    Boolean newExperiment;
    Boolean isOwner;
    String experimentId;
    String experimentType;
    ExperimentInfo experimentInfo;
    final String TAG = "ExperimentViewActivity";


    TextView regionText, descriptionText, ownerIdText, totalTrialsText;
    Button unpublishButton, endButton, addTrialButton, forumButton, subscribeButton;
    ListView trialList;
    ArrayList<Trial> trialDataList;
    ArrayAdapter<Trial> trialAdapter;

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
        regionText = findViewById(R.id.region_text);
        descriptionText = findViewById(R.id.description_text);
        ownerIdText = findViewById(R.id.ownerId_text);
        totalTrialsText = findViewById(R.id.totalTrials_text);

        regionText.setText("Region: " + experimentInfo.getRegion());
        descriptionText.setText("Description: " + experimentInfo.getDescription());

        // get owner name
        UserDAL userDAL = new UserDAL();
        userDAL.findUserByID(experimentInfo.getOwnerId(), new UserDAL.FindUserByIDCallback() {
            @Override
            public void onCallback(User user) {
                ownerIdText.setText("Owner: " + user.getUsername());
            }
        });

        // hides owner-function buttons if current user is not the owner
        unpublishButton = findViewById(R.id.unpublish_button);
        endButton = findViewById(R.id.end_button);
        subscribeButton = findViewById(R.id.subscribe_button);
        addTrialButton = findViewById(R.id.addTrial_button);
        if (!isOwner) {
            unpublishButton.setVisibility(View.INVISIBLE);
            endButton.setVisibility(View.INVISIBLE);

            if (experimentInfo.getStatus().equals("Closed")) {
                addTrialButton.setEnabled(false);
            }

            /* once user subscribing condition set up
            if () {
                subscribeButton.setText("Unsubscribe");
            }
            */

        } else {
            subscribeButton.setVisibility(View.INVISIBLE);

            // if already unpublished, sets button to allow re-publishing
            if (experimentInfo.getStatus().equals("Unpublish")) {
                unpublishButton.setText("Publish");

            } else if (experimentInfo.getStatus().equals("Closed")) {
                endButton.setText("Open");
                addTrialButton.setEnabled(false);
                // if permanently closed
                // endButton.setEnabled(false);
            }
        }

        unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog((String) unpublishButton.getText());
            }
        });

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog((String) subscribeButton.getText());
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog((String) endButton.getText());
            }
        });

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddTrialDialogFragment.newInstance(experimentType, experimentId);
                newFragment.show(getSupportFragmentManager(), "ADD TRIAL");
            }
        });

        // set up the list of trials
        trialList = findViewById(R.id.trial_listView);
        trialDataList = new ArrayList<>();
        trialAdapter = new CustomTrialList(this, trialDataList, experimentId);
        trialList.setAdapter(trialAdapter);
        ExperimentDAL experimentDAL = new ExperimentDAL();

        // create a document snapshot listener in the DAL to update the list of trials
        experimentDAL.addTrialListener(experimentId, trialDataList, trialAdapter, experimentType, totalTrialsText);

    }

    public void openForum(View view) {
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra(Extras.EXPERIMENT_ID, experimentId);
        startActivity(intent);
    }

    public void confirmationDialog(String tag) {
        new ConfirmationFragment(tag).show(getSupportFragmentManager(), "CONF");
    }

    // Action based on confirmation
    @Override
    public void onOkPressedConfirm(String tag) {
        if (tag.equals("Unpublish")) {
            unpublishButton.setText("Publish");
            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, "Unpublish");

        } else if (tag.equals("Publish")) {
            unpublishButton.setText("Unpublish");
            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, "Active");

        } else if (tag.equals("End")) {
            endButton.setText("Open");
            addTrialButton.setEnabled(false);

            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, "Closed");

        } else if (tag.equals("Open")) {
            endButton.setText("End");
            addTrialButton.setEnabled(true);

            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, "Active");

        } else if (tag.equals("Subscribe to")) {
            subscribeButton.setText(("Unsubscribe"));
            // change user subscription status

        } else if (tag.equals("Unsubscribe from")) {
            subscribeButton.setText(("Subscribe"));
            // change user subscription status

        }
    }

    // when back button is pressed
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