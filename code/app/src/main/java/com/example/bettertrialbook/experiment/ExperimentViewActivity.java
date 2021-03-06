package com.example.bettertrialbook.experiment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.forum.ForumActivity;
import com.example.bettertrialbook.home.MainActivity;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.User;
import com.example.bettertrialbook.profile.ProfileViewActivity;
import com.example.bettertrialbook.statistic.StatsNumber;

import java.util.ArrayList;

/**
 * Activity for viewing experiment details. Uses the ExperimentDAL to get the data
 * from the database. Uses the UserDAL to get the id of the user so that owner functions can
 * be hidden from non-owners.
 */
public class ExperimentViewActivity extends AppCompatActivity implements ConfirmationFragment.OnFragmentInteractionListener,
        TrialProfileFragment.OnFragmentInteractionListener, EditExperimentFragment.OnFragmentInteractionListener,GeoWarningFragment.OnFragmentInteractionListener {
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
    Button unpublishButton, endButton, addTrialButton, forumButton, subscribeButton, statButton;
    Button viewMapButton;
    ListView trialList;
    ArrayList<Trial> trialDataList;
    ArrayAdapter<Trial> trialAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);

        // set back button on actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("View Experiment");
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        regionText = findViewById(R.id.region_text);
        statButton = findViewById(R.id.stats_button);
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
                if (user != null) {
                    if (!user.getUsername().equals("")) {
                        ownerIdText.setText("Owner: " + user.getUsername());
                    } else {
                        ownerIdText.setText("Owner: " + user.getID().substring(0, 8));
                    }
                }
            }
        });

        // hides owner-function buttons if current user is not the owner
        unpublishButton = findViewById(R.id.unpublish_button);
        endButton = findViewById(R.id.end_button);
        subscribeButton = findViewById(R.id.subscribe_button);
        addTrialButton = findViewById(R.id.addTrial_button);
        String pStatus = experimentInfo.getPublishStatus();
        String aStatus = experimentInfo.getActiveStatus();
        viewMapButton = findViewById(R.id.viewMap_button);

        ExperimentDAL experimentDAL = new ExperimentDAL();
        if (!isOwner) {
            unpublishButton.setVisibility(View.INVISIBLE);
            endButton.setVisibility(View.INVISIBLE);

            if (aStatus != null && aStatus.equals("Closed")) {
                addTrialButton.setEnabled(false);
            }

            userDAL.isSubscribed(experimentId, You.getUser().getID(), new UserDAL.IsSubscribedCallback() {
                @Override
                public void onCallback(Boolean isSubscribed) {
                    if (isSubscribed) {
                        subscribeButton.setText("Unsubscribe");
                    } else {
                        addTrialButton.setEnabled(false);
                    }
                }
            });

            // adds listener to check in real-time if a user has been blacklisted
            experimentDAL.addBlacklistListener(experimentId, You.getUser().getID(),
                    new ExperimentDAL.IsBlacklistedCallback() {
                        @Override
                        public void onCallback(Boolean isBlacklisted) {
                            if (isBlacklisted) {
                                Toast.makeText(getApplicationContext(),
                                        String.format("You can no longer add trials to this experiment",
                                                experimentInfo.getDescription()),
                                        Toast.LENGTH_LONG).show();
                                addTrialButton.setEnabled(false);
                            } else {
                                addTrialButton.setEnabled(true);
                            }
                        }
                    });

            // adds listener to check in real-time for any status changes that affects add
            // trial or experiment visibility
            experimentDAL.addStatusListener(experimentId, new ExperimentDAL.IsOpenCallback() {
                @Override
                public void onCallback(String isOpen) {
                    if (isOpen.equals("Unpublish")) {
                        Toast.makeText(getApplicationContext(),
                                String.format("Experiment %s has been unpublished", experimentInfo.getDescription()),
                                Toast.LENGTH_LONG).show();
                        onBackPressed();

                    } else if (isOpen.equals("Closed")) {
                        Toast.makeText(getApplicationContext(), "This experiment has been closed", Toast.LENGTH_SHORT)
                                .show();
                        addTrialButton.setEnabled(false);

                    } else if (isOpen.equals("Active")) {
                        Toast.makeText(getApplicationContext(), "This experiment has been reopened", Toast.LENGTH_SHORT)
                                .show();
                        addTrialButton.setEnabled(true);
                    }
                }
            });

        } else {
            subscribeButton.setVisibility(View.INVISIBLE);

            // if already unpublished, sets button to allow re-publishing
            if (pStatus != null && pStatus.equals("Unpublish")) {
                unpublishButton.setText("Publish");

            } else if (aStatus != null && aStatus.equals("Closed")) {
                endButton.setText("Open");
                addTrialButton.setEnabled(false);
                // if permanently closed
                // endButton.setEnabled(false);
            }
        }

        // all the onClick listeners
        if (isOwner) {
            descriptionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editExperiment();
                }
            });

            regionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editExperiment();
                }
            });
        }

        unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog((String) unpublishButton.getText(), false, "");
            }
        });

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog((String) subscribeButton.getText(), false, "");
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog((String) endButton.getText(), false, "");
            }
        });

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (experimentInfo.getGeoLocationRequired()) {
                    new GeoWarningFragment("WARNING!\nGEOLOCATIONS ARE REQUIRED!").show(getSupportFragmentManager(), "WARNING");
                }

                else {
                    DialogFragment newFragment = AddTrialDialogFragment.newInstance(experimentType, experimentId,
                            geolocationRequired);
                    newFragment.show(getSupportFragmentManager(), "ADD TRIAL");
                }
            }
        });

        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statisticReport();
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("GeoLocation", geoLocations);
                intent.putExtras(bundle);
                intent.putExtra("allLocations", true); // tell the map activity to display geolocations instead of allowing selection
                startActivity(intent);
            }
        });

        // set up the list of trials
        trialList = findViewById(R.id.trial_listView);
        trialDataList = new ArrayList<>();
        trialAdapter = new CustomTrialList(this, trialDataList, experimentId, isOwner);
        trialList.setAdapter(trialAdapter);
        trialList.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 String experimenterID = trialAdapter.getItem(position).getExperimenterID();
                 Trial trial = trialAdapter.getItem(position);
                 new TrialProfileFragment(isOwner, trial, experimentId).show(getSupportFragmentManager(), "PROFILE");
             }
         });

        // create a document snapshot listener in the DAL to update the list of trials
        experimentDAL.addTrialListener(experimentId, trials -> {
            trialDataList.clear();
            trialDataList.addAll(trials);
            totalTrialsText.setText("Total Trials: " + Integer.toString(trialAdapter.getCount()));
            trialAdapter.notifyDataSetChanged();
        });

    }

    /**
     * Edits experiment information like description and region
     */
    private void editExperiment() {
        String description = (String)descriptionText.getText();
        String region = (String) regionText.getText();
        description = description.replace("Description: ", "");
        region = region.replace("Region: ", "");
        new EditExperimentFragment(description, region).show(getSupportFragmentManager(), "EDITEXP");
    }

    @Override
    public void onOkPressed(String description, String region, Boolean delete) {
        if (!delete) {
            // update TextView with new description and region
            descriptionText.setText("Description: " + description);
            regionText.setText("Region: " + region);

            // update firebase with new description and region
            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentDescription(experimentId, description);
            experimentDAL.setExperimentRegion(experimentId, region);

        } else {
            confirmationDialog("Delete", false, "");
        }
    }

    /**
     * Opening up confirmation fragment and action on callback
     *
     * @param tag            the tag to determine what text displayed in
     *                       confirmation
     * @param blacklist      if this is a blacklist fragment or not
     * @param experimenterID experimenter's id for blacklist logic
     */
    public void confirmationDialog(String tag, Boolean blacklist, String experimenterID) {
        new ConfirmationFragment(tag, blacklist, experimenterID).show(getSupportFragmentManager(), "CONF");
    }

    // Action based on confirmation
    @Override
    public void onOkPressedConfirm(String tag, String experimenterID) {
        String pStatus = "PublishStatus";
        String aStatus = "ActiveStatus";
        if (tag.equals("Unpublish")) {
            unpublishButton.setText("Publish");
            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, pStatus, "Unpublish");

        } else if (tag.equals("Publish")) {
            unpublishButton.setText("Unpublish");
            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, pStatus, "Publish");

        } else if (tag.equals("End")) {
            endButton.setText("Open");
            addTrialButton.setEnabled(false);

            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, aStatus, "Closed");

        } else if (tag.equals("Open")) {
            endButton.setText("End");
            addTrialButton.setEnabled(true);

            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.setExperimentStatus(experimentId, aStatus, "Active");

        } else if (tag.equals("Subscribe to")) {
            subscribeButton.setText(("Unsubscribe"));
            addTrialButton.setEnabled(true);

            UserDAL userDAL = new UserDAL();
            userDAL.subscribeExperiment(experimentId, You.getUser().getID());

        } else if (tag.equals("Unsubscribe from")) {
            subscribeButton.setText(("Subscribe"));
            addTrialButton.setEnabled(false);

            UserDAL userDAL = new UserDAL();
            userDAL.unsubscribeExperiment(experimentId, You.getUser().getID());

        } else if (tag.equals("block")) {
            ExperimentDAL experimentDAL = new ExperimentDAL();
            experimentDAL.modifyBlacklist(experimentId, experimenterID, true);

        } else if (tag.equals("Delete")) {
            ExperimentDAL experimentDAL = new ExperimentDAL();
            UserDAL userDAL = new UserDAL();
            experimentDAL.deleteExperiment(experimentId);
            userDAL.unsubscribeExperiment(experimentId, experimentInfo.getOwnerId());
            Toast.makeText(getApplicationContext(),
                    String.format("Experiment %s has been deleted", experimentInfo.getDescription()),
                    Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    @Override
    public void onBlacklist(String experimenterId) {
        confirmationDialog("block", true, experimenterId);
    }

    // navigation between activities
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @param view - Navigate to forum
     */
    public void openForum(View view) {
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra(Extras.EXPERIMENT_ID, experimentId);
        startActivity(intent);
    }

    /**
     * Prepare data for statistics page
     */
    public void statisticReport() {
        Intent intent = new Intent(this, StatsNumber.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Trials", trialDataList);
        intent.putExtras(bundle);
        intent.putExtra("TrialType", experimentType);
        startActivity(intent);
    }

    @Override
    public void onViewProfile() {
        viewProfile(findViewById(android.R.id.content).getRootView());
    }

    // when back button is pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* Get return object ready  */
        if (newExperiment) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        } else {
            finish();
        }
        return true;
    }

    /**
     * @param view - When clicking a username from experiment view, open up their
     *             profile
     */
    public void viewProfile(View view) {
        Intent intent;
        intent = new Intent(this, ProfileViewActivity.class);

        // Are 'You' the owner, send in 'You' object
        if (isOwner) {
            intent.putExtra("User", You.getUser());
            startActivity(intent);

            // Else, create a user object from the owner's ID and send it in to activity
        } else {
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
    // Action based on confirmation
    @Override
    public void onContPressed() {
        DialogFragment newFragment = AddTrialDialogFragment.newInstance(experimentType, experimentId,
                geolocationRequired);
        newFragment.show(getSupportFragmentManager(), "ADD TRIAL");
    }
}
