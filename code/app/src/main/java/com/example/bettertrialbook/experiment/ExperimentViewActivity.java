/*
    Activity for viewing experiment details. Uses the ExperimentDAL to get the data
    from the database. Uses the UserDAL to get the id of the user so that owner functions can
    be hidden from non-owners.
    TODO: QR functionality, blacklist user's trials, geolocation, view other's profile, statistics
 */
package com.example.bettertrialbook.experiment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.home.MainActivity;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.forum.ForumActivity;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.User;
import com.example.bettertrialbook.profile.ProfileViewActivity;

import java.util.ArrayList;

public class ExperimentViewActivity extends AppCompatActivity
        implements ConfirmationFragment.OnFragmentInteractionListener {
    Boolean newExperiment;
    Boolean isOwner;
    String experimentId;
    String experimentType;
    boolean geolocationRequired;
    String ownerId;
    ExperimentInfo experimentInfo;
    final String TAG = "ExperimentViewActivity";

    public TextView regionText, descriptionText, ownerIdText, totalTrialsText, setting;
    Button createQRButton;
    Button unpublishButton, endButton, addTrialButton, forumButton, subscribeButton, viewMapButton;
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
        ownerId = getIntent().getStringExtra("OwnerId");

        experimentId = experimentInfo.getId();
        experimentType = experimentInfo.getTrialType();
        geolocationRequired = experimentInfo.getGeoLocationRequired();
        Log.d("view", String.valueOf(geolocationRequired));

        // Populates experiment page with relevant text
        setting = findViewById(R.id.setting);
        regionText = findViewById(R.id.region_text);
        descriptionText = findViewById(R.id.description_text);
        ownerIdText = findViewById(R.id.ownerId_text);
        totalTrialsText = findViewById(R.id.totalTrials_text);
        createQRButton = findViewById(R.id.createQR_button);

        regionText.setText("Region: " + experimentInfo.getRegion());
        setting.setPaintFlags(setting.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
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
        viewMapButton = findViewById(R.id.viewMap_button);
        if (experimentInfo.getGeoLocationRequired()) {
            addTrialButton.setText("Add Trial\n(Geolocation\nRequired)");
        }
        if (!isOwner) {
            unpublishButton.setVisibility(View.INVISIBLE);
            endButton.setVisibility(View.INVISIBLE);

            if (experimentInfo.getStatus().equals("Closed")) {
                addTrialButton.setEnabled(false);
            }

            /*
             * once user subscribing condition set up if () {
             * subscribeButton.setText("Unsubscribe"); }
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
                DialogFragment newFragment = AddTrialDialogFragment.newInstance(experimentType, experimentId, geolocationRequired);
                newFragment.show(getSupportFragmentManager(), "ADD TRIAL");
            }
        });

        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all geoLocations
                ArrayList<Geolocation> geoLocations = new ArrayList<>();
                Log.d("view,", "size: " + trialDataList.size());
                // create an arraylist of all the geolocations
                for (int i = 0; i < trialDataList.size(); i++) {
                    Log.d("view", String.valueOf(i));
                    geoLocations.add(trialDataList.get(i).getGeolocation());
                    Log.d("Geolocation1", String.valueOf(trialDataList.get(i).getGeolocation()));
                }

                Intent intent = new Intent(ExperimentViewActivity.this, GeolocationActivity.class);
                intent.putExtra("allLocations", true);  // tell the map activity to display instead of select
                intent.putParcelableArrayListExtra("geoLocations", geoLocations);
                startActivity(intent);
            }
        });

        // set up the list of trials
        trialList = findViewById(R.id.trial_listView);
        trialDataList = new ArrayList<>();
        trialAdapter = new CustomTrialList(this, trialDataList, experimentId, isOwner);
        trialList.setAdapter(trialAdapter);
//        trialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Trial trial = trialDataList.get(position);
//                String experimenterId = trial.getExperimenterID();
//                boolean isTrialOwner = experimenterId.equals(ownerId);
//
//                Log.d("view", String.valueOf(trial.getGeolocation().getLocation().getLatitude()));
//                Log.d("view", String.valueOf(trial.getGeolocation().getLocation().getLongitude()));
//
//                Intent intent = new Intent(ExperimentViewActivity.this, GeolocationActivity.class);
//                intent.putExtra("IsTrialOwner", isTrialOwner);
//                startActivity(intent);
//            }
//        });
        ExperimentDAL experimentDAL = new ExperimentDAL();

        // create a document snapshot listener in the DAL to update the list of trials
        experimentDAL.addTrialListener(experimentId, experimentType, trials -> {
            trialDataList.clear();
            trialDataList.addAll(trials);
            totalTrialsText.setText("Total Trials: " + Integer.toString(trialAdapter.getCount()));
            trialAdapter.notifyDataSetChanged();
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


    /**
     * @param view - When clicking a username from experiment view, open up their profile
     */
    public void viewProfile(View view){
        Intent intent;
        intent = new Intent(this, ProfileViewActivity.class);

        //Are 'You' the owner, send in 'You' object
        if(isOwner){
            intent.putExtra("User",You.getUser());
            startActivity(intent);

        //Else, create a user object from the owner's ID and send it in to activity
        }else {
            UserDAL uDAL = new UserDAL();
            uDAL.findUserByID(experimentInfo.getOwnerId(), new UserDAL.FindUserByIDCallback() {
                @Override
                public void onCallback(User user) {
                    Log.d("TEST", "User Found:" + user.getID() + user.getUsername());
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
            });
        }
    }
}