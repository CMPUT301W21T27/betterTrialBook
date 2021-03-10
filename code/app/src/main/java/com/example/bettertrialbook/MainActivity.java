/*
    Initial Screen
    Current Version: V1.1
 */
package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import android.content.Context;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;

import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

import java.util.UUID;
import java.util.ArrayList;

import com.example.bettertrialbook.models.ExperimentInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    User you;
    UserDAL uDAL;
    private ArrayList<ExperimentInfo> trialInfoList;
    private ArrayAdapter<ExperimentInfo> trialInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanQR = findViewById(R.id.ScanQR_Button);               // Button to go to the scan QR page
        Button createQR = findViewById(R.id.CreateQR_Button);           // Button to go to the create QR page
        ListView resultList = findViewById(R.id.Result_ListView);
        SearchView searchItem = findViewById(R.id.SearchItem);

        trialInfoList = new ArrayList<>();
        trialInfoAdapter = new CustomList(this, trialInfoList);
        resultList.setAdapter(trialInfoAdapter);

        // Connection to the FireStore FireBase
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
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
                            // Further search method will be refined after I have figured out what kind of search we are gonna do
                            if (checkforKeyWords((String) doc.getData().get("Description"), newText)) {
                                // MinTrials hasn't done yet. Want to wait for further production and then decide.
                                // GeoLocationRequired hasn't done yet. Want to wait for further production and then decide.
                                String id = doc.getId();
                                String region = (String) doc.getData().get("Region");
                                String status = (String) doc.getData().get("Status");
                                String trialType = (String) doc.getData().get("TrialType");
                                String description = (String) doc.getData().get("Description");
                                trialInfoList.add(new ExperimentInfo(id, description, status, trialType, false, 0, region));
                            }
                        }
                    } else {
                        trialInfoList.clear();
                    }
                    trialInfoAdapter.notifyDataSetChanged();
                });
                return false;
            }});

        generateID();
    }

    public void viewYourProfile(View view) {
        /*Calls the ProfileViewActivity
         * Sends user object "you" to display their info
         * Expects an updated "you" object as a return
         * */
        Intent intent = new Intent(this, ProfileViewActivity.class);
        intent.putExtra("User", you);
        startActivityForResult(intent, 1);
    }

    public void generateID() {
        /*
         * Generates a unqiue ID per user
         * Checks if ID is in database
         *   Adds ID to DB if it's not
         * Creates user object "you" to represent you
         * */

        // Generating ID
        // https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values
        SharedPreferences sharedPref = this.getSharedPreferences("uniqueID", Context.MODE_PRIVATE);
        String defaultIDValue = sharedPref.getString("uniqueID", null);
        if (defaultIDValue == null) {
            Log.d("Generate", "Empty id");
            String uID = UUID.randomUUID().toString();
            Log.d("Generate", uID.toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("uniqueID", uID);
            editor.apply();
            defaultIDValue = uID;

        } else {
            Log.d("TEST", "1. " + defaultIDValue);
        }

        // Checking if ID in Database, else adds it to database
        final String finalID = defaultIDValue;
        uDAL = new UserDAL();

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

    //Return from ProfileViewActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        you = intent.getExtras().getParcelable("User");   //Accessing Parcelable Objects

    }

    public void createExperiment(View view) {
        Intent intent = new Intent(this, ExperimentAddActivity.class);
        startActivity(intent);
    }

    // Check for the keywords in the description of the document in the FireBase FireStore
    public boolean checkforKeyWords(String word, String key) {
        return word.toLowerCase().contains(key.toLowerCase());
    }
}
