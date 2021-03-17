/*
    Initial Screen
    Current Version: V1.1
 */
package com.example.bettertrialbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    User you;
    UserDAL uDAL = new UserDAL();
    private ArrayList<ExperimentInfo> trialInfoList;
    private ArrayAdapter<ExperimentInfo> trialInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanQR = findViewById(R.id.ScanQR_Button); // Button to go to the scan QR page
        Button create = findViewById(R.id.CreateQR_Button); // Button to go to the create Experiment
        SearchView searchItem = findViewById(R.id.SearchItem);
        ImageView profilePic = findViewById(R.id.ProfilePicture); // Used to go to the profile page
        ListView resultList = findViewById(R.id.Result_ListView);

        trialInfoList = new ArrayList<>();
        trialInfoAdapter = new CustomList(this, trialInfoList);
        resultList.setAdapter(trialInfoAdapter);
        resultList.setOnItemClickListener(this);

        generateID();

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

        // Search the Result (Will Refine)
        FirebaseFirestore db;
        db = Firestore.getInstance();
        CollectionReference reference = db.collection("Experiments");
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Empty Body: May add-on new features
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
                                String status = (String) doc.getData().get("Status");
                                if ((ownerId != null && ownerId.equals(you.getID()))
                                        || (status != null && !status.equals("Unpublished"))) {
                                    String id = doc.getId();
                                    String region = (String) doc.getData().get("Region");
                                    String trialType = (String) doc.getData().get("TrialType");
                                    trialInfoList.add(new ExperimentInfo(description, ownerId, status, id, trialType,
                                            false, 0, region));
                                }
                            }
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

    public void viewYourProfile(View view) {
        /*
         * Calls the ProfileViewActivity Sends user object "you" to display their info
         * Expects an updated "you" object as a return
         */
        Intent intent = new Intent(this, ProfileViewActivity.class);
        intent.putExtra("User", you);
        startActivityForResult(intent, 1);
    }

    // Return from ProfileViewActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        you = intent.getExtras().getParcelable("User"); // Accessing Parcelable Objects

    }

    public void createExperiment(View view) {
        Intent intent = new Intent(this, ExperimentAddActivity.class);
        intent.putExtra("OwnerId", you.getID());
        startActivity(intent);
    }

    public void generateID() {
        /*
         * Generates a unique ID per user Checks if ID is in database Adds ID to DB if
         * it's not Creates user object "you" to represent you
         */

        // Generating ID
        String defaultIDValue = uDAL.getDeviceUserId(this);
        if (defaultIDValue == null) {
            Log.d("Generate", "Empty id");
            String uID = UUID.randomUUID().toString();
            Log.d("Generate", uID.toString());

            uDAL.setDeviceUserId(this, uID);
            defaultIDValue = uID;

        } else {
            Log.d("TEST", "1. " + defaultIDValue);
        }

        // Checking if ID in Database, else adds it to database
        final String finalID = defaultIDValue;

        // https://www.youtube.com/watch?v=0ofkvm97i0s - Callback
        uDAL.findUserByID(defaultIDValue, new UserDAL.FindUserByIDCallback() {
            @Override
            public void onCallback(User user) {
                // If no user found, create user
                if (user == null) {
                    you = uDAL.addUser(finalID);
                } else {
                    Log.d("TEST", "4. " + user.getID() + user.getUsername());
                    you = user;
                }
            }
        });
    }

    // Goes to experiment's page if clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Boolean isOwner = ((trialInfoAdapter.getItem(position).getOwnerId()).equals(you.getID()));
        ExperimentInfo experimentInfo = trialInfoAdapter.getItem(position);

        Intent myIntent = new Intent(view.getContext(), ExperimentViewActivity.class);
        myIntent.putExtra("IsOwner", isOwner);
        myIntent.putExtra("NewExperiment", false);
        myIntent.putExtra("ExperimentInfo", experimentInfo);
        startActivity(myIntent);
    }
}
