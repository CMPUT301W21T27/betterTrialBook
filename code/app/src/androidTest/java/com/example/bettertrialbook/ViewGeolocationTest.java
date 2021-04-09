package com.example.bettertrialbook;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.experiment.ExperimentAddActivity;
import com.example.bettertrialbook.experiment.ExperimentViewActivity;
import com.example.bettertrialbook.experiment.GeolocationActivity;
import com.example.bettertrialbook.home.MainActivity;
import com.example.bettertrialbook.models.Experiment;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.profile.ProfileViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

/**
* UI Test for testing geolocation functionality and visibility
 */
public class ViewGeolocationTest {
    private Solo solo;
    public ActivityTestRule<MainActivity> rule;
    FirebaseFirestore db;
    String experimentName = "Geolocation Test";
    String experimenterName = "GeolocationTester";


    @Rule
    public TestRule setRules() {
        Firestore.useEmulator();
        rule = new ActivityTestRule<>(MainActivity.class, true, true);
        return rule;
    }

    @Before
    public void setUp() throws Exception {
        db = Firestore.getInstance();
        solo = new Solo(getInstrumentation(), rule.getActivity());
        String activityMessage = "Wrong Activity";
        solo.assertCurrentActivity(activityMessage,MainActivity.class);

        createUser();
        createExperiment();

        solo.sleep(5000);   //wait for database access to finish

        solo.clickOnImage(0);
        solo.enterText(0,experimentName);
        solo.waitForText(experimentName);

        solo.clickInList(0);
        solo.assertCurrentActivity(activityMessage, ExperimentViewActivity.class);

        //First subscribe to enable adding trials
        solo.clickOnButton("Subscribe");
        solo.waitForText("OK");
        solo.clickOnText("OK");
        solo.sleep(2000);
    }

    /**
     * Test to see if pressing View Map brings you to the GeolocationActivity activity
     */
    @Test
    public void start() {
        solo.clickOnButton("View Map");

        // Check to make sure class switched
        solo.assertCurrentActivity("Wrong Activity", GeolocationActivity.class);
        solo.sleep(2000);

        solo.clickOnButton("Back");
        solo.assertCurrentActivity("Wrong Activity", ExperimentViewActivity.class);
        solo.sleep(2000);
    }


    /**
     * Test to see if adding a trial brings you to the GeolocationActivity activity
     */
    @Test
    public void addTrialMap() {
        //Add a Trial and handle the warning popup
        solo.clickOnButton("Add Trial");
        solo.waitForText("Continue");
        solo.clickOnButton("Continue");

        //Fill out the trial information
        solo.waitForText("Non-Negative Integer");
        solo.enterText((EditText) solo.getView(R.id.intamount_editText),"42");

        //Add a geolocation to the trial
        solo.clickOnButton("Add Geolocation");
        solo.waitForActivity(GeolocationActivity.class);

        // If location permissions are required, select "Only this time"
        if (solo.searchText("Only this time")) {
            solo.clickOnText("Only this time");
        }

        // Check if the geolocation activity properly loaded
        solo.assertCurrentActivity("Wrong Activity", GeolocationActivity.class);
    }


    /**
     * Add a trial and see if the geolocation gets stored properly
     */
    @Test
    public void storeTrialGeolocation() {
        //Add a Trial and handle the warning popup
        solo.clickOnButton("Add Trial");
        solo.waitForText("Continue");
        solo.clickOnButton("Continue");

        //Fill out the trial information
        solo.waitForText("Non-Negative Integer");
        solo.enterText((EditText) solo.getView(R.id.intamount_editText),"42");

        //Add a geolocation to the trial
        solo.clickOnButton("Add Geolocation");
        solo.waitForActivity(GeolocationActivity.class);

        // If location permissions are required, select "Only this time"
        if (solo.searchText("Only this time")) {
            solo.clickOnText("Only this time");
        }

        solo.assertCurrentActivity("Wrong Activity", GeolocationActivity.class);
        solo.sleep(2000);


        // Add the trial to the experiment
        solo.clickOnButton("SELECT");

        solo.waitForText("OK");
        solo.clickOnButton("OK");

        solo.waitForActivity(ExperimentViewActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ExperimentViewActivity.class);

        //Wait for database to update
        solo.sleep(2000);

        // Check if the geolocation exists in the Firestore database
        db.collection("Experiments").whereEqualTo("Description", experimentName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<HashMap<Object, Object>> trials = (ArrayList<HashMap<Object, Object>>) document.getData().get("Trials");
                                assertNotNull(trials.get(0).get("geolocation"));
                            }
                        } else {
                            Log.d("TEST", "Error getting documents to check for geolocation: ", task.getException());
                        }
                    }
                });
    }



    /**
     * A generic user
     */
    public void createUser(){
        CollectionReference colRef = db.collection("Users");

        Map<String, String> data = new HashMap<>();
        data.put("email", "");
        data.put("phone", "");
        data.put("username", experimenterName);

        colRef.document(experimenterName).set(data);
        solo.sleep(5000);
    }

    /**
     * The generic users experiment
     */
    public void createExperiment() {
        //Creates a generic experiment

        CollectionReference colRef = db.collection("Experiments");

        Map<String, Object> data = new HashMap<>();
        data.put("ActiveStatus", "Publish");
        data.put("Description", experimentName);
        data.put("GeoLocationRequired", true);
        data.put("MinTrials", 200);
        data.put("Owner", experimenterName);
        data.put("PublishStatus", "Active");
        data.put("Region", "Edmonton");
        data.put("TrialType", "Non-Negative Integer");

        colRef.document("GeolocationExperiment").set(data);
        solo.sleep(5000);
    }

    @After
    public void tearDown(){
        //Delete users and experiment from emulator
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", document.getId() + " => " + document.getData());
                                db.collection("Users").document(document.getId()).delete();
                            }
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("Experiments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", document.getId() + " => " + document.getData());
                                db.collection("Experiments").document(document.getId()).delete();
                            }
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });

        //Close app
        solo.finishOpenedActivities();
    }
}
