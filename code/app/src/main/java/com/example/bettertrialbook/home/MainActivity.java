/*
    Initial Screen
    TODO: Create QR Functionality
 */
package com.example.bettertrialbook.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.experiment.ExperimentAddActivity;
import com.example.bettertrialbook.experiment.ExperimentViewActivity;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.User;
import com.example.bettertrialbook.profile.ProfileViewActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The main activity hosts buttons to create new experiments, view your profile and search experiments.
 * Limitations: We will aim to move the search logic out of this activity and into the ExperimentDAL
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    UserDAL uDAL = new UserDAL();
    private ArrayList<ExperimentInfo> trialInfoList;
    private ArrayAdapter<ExperimentInfo> trialInfoAdapter;
    Boolean neverSearched = true;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanQR = findViewById(R.id.ScanQR_Button); // Button to go to the scan QR page
        Button create = findViewById(R.id.CreateQR_Button); // Button to go to the create Experiment
        SearchView searchItem = findViewById(R.id.SearchItem);
        ImageView profilePic = findViewById(R.id.ProfilePicture); // Used to go to the profile page
        ListView resultList = findViewById(R.id.Result_ListView);

        userId = generateID();

        trialInfoList = new ArrayList<>();
        trialInfoAdapter = new ExperimentList(this, trialInfoList);
        resultList.setAdapter(trialInfoAdapter);
        resultList.setOnItemClickListener(this);

        // Go to the Profile View Screen
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewYourProfile(v);
            }
        });

        // Go to Create Experiment Screen
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExperiment(v);
            }
        });

        // Search the Result
        FirebaseFirestore db;
        db = Firestore.getInstance();
        CollectionReference reference = db.collection("Experiments");

        // initial display of all subscribed experiments
        if (userId != null) {
            uDAL.getSubscribed(userId, new UserDAL.GetSubscribedCallback() {
                @Override
                public void onCallback(List<String> subscribed) {
                    if (subscribed != null) {
                        for (int i = 0; i < subscribed.size(); i++) {
                            ExperimentDAL experimentDAL = new ExperimentDAL();
                            experimentDAL.findExperimentByID(subscribed.get(i), new ExperimentDAL.FindExperimentByIDCallback() {
                                @Override
                                public void onCallback(ExperimentInfo experimentInfo) {
                                    if (experimentInfo != null) {
                                        trialInfoList.add(experimentInfo);
                                        trialInfoAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Empty Body: May add-on new features
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                neverSearched = false;
                reference.addSnapshotListener((queryDocumentSnapshots, error) -> {
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
                                if ((ownerId != null && You.getUser() != null && ownerId.equals(You.getUser().getID()))
                                        || (publishStatus != null && !publishStatus.equals("Unpublish"))) {
                                    String id = doc.getId();
                                    String region = (String) doc.getData().get("Region");
                                    String trialType = (String) doc.getData().get("TrialType");
                                    trialInfoList.add(new ExperimentInfo(description, ownerId, publishStatus, activeStatus,
                                            id, trialType, false, 0, region));
                                }
                            }
                        }

                    } else if (newText.length() == 0) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String experimentId = (String) doc.getId();

                            uDAL.isSubscribed(experimentId, You.getUser().getID(), new UserDAL.IsSubscribedCallback() {
                                @Override
                                public void onCallback(Boolean isSubscribed) {
                                    if (isSubscribed) {
                                        // MinTrials hasn't done yet. Want to wait for further production and then
                                        // decide.
                                        // GeoLocationRequired hasn't done yet. Want to wait for further production and
                                        // then decide.
                                        Log.d("TEST2", String.valueOf(experimentId));
                                        String ownerId = (String) doc.getData().get("Owner");
                                        String publishStatus = (String) doc.getData().get("PublishStatus");
                                        String activeStatus = (String) doc.getData().get("ActiveStatus");
                                        if ((ownerId != null && ownerId.equals(You.getUser().getID()))
                                                || (publishStatus != null && !publishStatus.equals("Unpublished"))) {
                                            String id = doc.getId();
                                            String description = (String) doc.getData().get("Description");
                                            String region = (String) doc.getData().get("Region");
                                            String trialType = (String) doc.getData().get("TrialType");
                                            trialInfoList.add(new ExperimentInfo(description, ownerId, publishStatus, activeStatus,
                                                    id, trialType, false, 0, region));
                                        }

                                        trialInfoAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }

                    } else {
                        trialInfoList.clear();
                    }
                    trialInfoAdapter.notifyDataSetChanged();
                });
                return false;
            }
        });
    }

    /*
     * Calls the ProfileViewActivity Sends user object "you" to display their info
     * Expects an updated "you" object as a return
     *
     */
    public void viewYourProfile(View view) {
        Intent intent = new Intent(this, ProfileViewActivity.class);
        intent.putExtra("User",You.getUser());
        startActivity(intent);
    }

    public void createExperiment(View view) {
        Intent intent = new Intent(this, ExperimentAddActivity.class);
        intent.putExtra("OwnerId", You.getUser().getID());
        startActivity(intent);
    }

    public String generateID() {
        /*
         * Generates a unique ID per user Checks if ID is in database Adds ID to DB if
         * it's not Creates user object "you" to represent you
         */
        String userId = "";
        // Generating ID
        String defaultIDValue = uDAL.getDeviceUserId(this);
        if (defaultIDValue == null) {
            Log.d("Generate", "Empty id");
            String uID = UUID.randomUUID().toString();
            Log.d("Generate", uID.toString());

            uDAL.setDeviceUserId(this, uID);
            defaultIDValue = uID;

        } else {
            userId = defaultIDValue;
        }

        // Checking if ID in Database, else adds it to database
        final String finalID = defaultIDValue;

        // https://www.youtube.com/watch?v=0ofkvm97i0s - Callback
        uDAL.findUserByID(defaultIDValue, new UserDAL.FindUserByIDCallback() {
            @Override
            public void onCallback(User user) {
                // If no user found, create user
                if (user == null) {
                    You.setUser(uDAL.addUser(finalID));
                } else {
                    Log.d("TEST", "4. " + user.getID() + user.getUsername());
                    You.setUser(user);
                }
            }
        });

        return userId;
    }

    // Goes to experiment's page if clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Boolean isOwner = ((trialInfoAdapter.getItem(position).getOwnerId()).equals(You.getUser().getID()));
        String experimentId = trialInfoAdapter.getItem(position).getId();

        ExperimentDAL experimentDAL = new ExperimentDAL();
        experimentDAL.findExperimentByID(experimentId, new ExperimentDAL.FindExperimentByIDCallback() {
            @Override
            public void onCallback(ExperimentInfo experimentInfo) {
                Intent myIntent = new Intent(view.getContext(), ExperimentViewActivity.class);
                myIntent.putExtra("IsOwner", isOwner);
                myIntent.putExtra("NewExperiment", false);
                myIntent.putExtra("ExperimentInfo", experimentInfo);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (neverSearched) {
            trialInfoList.clear();
            if (userId != null) {
                uDAL.getSubscribed(userId, new UserDAL.GetSubscribedCallback() {
                    @Override
                    public void onCallback(List<String> subscribed) {
                        Log.d("TEST2", String.valueOf(subscribed));
                        if (subscribed != null) {
                            if (subscribed.size() == 0) {
                                trialInfoAdapter.notifyDataSetChanged();
                            } else {
                                for (int i = 0; i < subscribed.size(); i++) {
                                    ExperimentDAL experimentDAL = new ExperimentDAL();
                                    experimentDAL.findExperimentByID(subscribed.get(i), new ExperimentDAL.FindExperimentByIDCallback() {
                                        @Override
                                        public void onCallback(ExperimentInfo experimentInfo) {
                                            trialInfoList.add(experimentInfo);
                                            trialInfoAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
