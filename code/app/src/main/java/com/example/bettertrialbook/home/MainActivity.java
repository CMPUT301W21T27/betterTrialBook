/*
    TODO: Create QR Functionality
 */
package com.example.bettertrialbook.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.bettertrialbook.qr.ScanQRActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The main activity hosts buttons to create new experiments, view your profile and search experiments.
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

        Toolbar toolbar = findViewById(R.id.ToolBar);
        Button create = findViewById(R.id.CreateQR_Button);
        Spinner itemSpinner = findViewById(R.id.ItemSpinner);
        Spinner trialSpinner = findViewById(R.id.TrialSpinner);
        SearchView searchItem = findViewById(R.id.SearchItem);
        ListView resultList = findViewById(R.id.Result_ListView);
        // For Targeting User or Description
        // For the User: it searches the id of the documentation on FireStore
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.SearchTarget, android.R.layout.simple_spinner_item);
        // For Targeting the experiment's type
        // No effect at the moment
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.TrialTarget, android.R.layout.simple_spinner_item);

        setSupportActionBar(toolbar);

        itemSpinner.setAdapter(adapter1);
        itemSpinner.setSelection(1);
        trialSpinner.setAdapter(adapter2);
        trialSpinner.setSelection(0);


        userId = generateID();

        trialInfoList = new ArrayList<>();
        trialInfoAdapter = new ExperimentList(this, trialInfoList);
        resultList.setAdapter(trialInfoAdapter);
        resultList.setOnItemClickListener(this);

        // populate page with published experiments


        // Go to Create Experiment Screen
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExperiment(v);
            }
        });

        // Search the Result by QueryTextChange
        FirebaseFirestore db;
        db = Firestore.getInstance();
        CollectionReference reference = db.collection("Experiments");

        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Empty Body
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ExperimentDAL experimentDAL = new ExperimentDAL();
                if (itemSpinner.getSelectedItem().toString().equalsIgnoreCase("Description")) {
                    experimentDAL.searchByDescription(trialInfoList, trialInfoAdapter, newText);
                }
                else if (itemSpinner.getSelectedItem().toString().equalsIgnoreCase("UserId")) {
                    experimentDAL.searchByUser(trialInfoList, trialInfoAdapter, newText);
                }
                return false;
            }
        });
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
        /*
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
                                            if (experimentInfo != null &&
                                                    (experimentInfo.getPublishStatus().equals("Publish") ||
                                                            experimentInfo.getOwnerId().equals(You.getUser().getID()))) {
                                                trialInfoList.add(experimentInfo);
                                                trialInfoAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
         */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            /*
             * Calls the ProfileViewActivity Sends user object "you" to display their info
             */
            case R.id.Profile:
                Intent intent1 = new Intent(this, ProfileViewActivity.class);
                intent1.putExtra("User",You.getUser());
                startActivity(intent1);
                return true;
            /*
             * Display what the user has subscribed
             */
            case R.id.Subscription:
                Intent intent2 = new Intent(this, SubscriptionActivity.class);
                intent2.putExtra("UserID", userId);
                intent2.putExtra("Boolean", neverSearched);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openQRScanner(View v){
        startActivity(new Intent(this, ScanQRActivity.class));
    }
}
