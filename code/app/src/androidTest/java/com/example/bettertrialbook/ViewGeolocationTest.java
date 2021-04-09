package com.example.bettertrialbook;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.experiment.ExperimentAddActivity;
import com.example.bettertrialbook.experiment.ExperimentViewActivity;
import com.example.bettertrialbook.experiment.GeolocationActivity;
import com.example.bettertrialbook.home.MainActivity;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.profile.ProfileViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

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
        solo.assertCurrentActivity("Wrong Activity", ExperimentViewActivity.class);
        solo.sleep(5000);
        solo.clickOnButton("View Map");
        solo.assertCurrentActivity("Wrong Activity", GeolocationActivity.class);
        solo.sleep(2000);

        // Check to make sure that the class switched
    }


    /**
     * Test to see if adding a trial brings you to the GeolocationActivity activity
     */
    @Test
    public void addTrialMap() throws UiObjectNotFoundException {
        solo.assertCurrentActivity("Wrong Activity", GeolocationActivity.class);
        solo.sleep(5000);
        solo.clickOnButton("CANCEL");
        solo.assertCurrentActivity("Wrong Activity", ExperimentViewActivity.class);

        solo.clickOnText("CONTINUE");
        solo.sleep(2000);
        solo.clickOnButton("ADD GEOLOCATION");
        solo.sleep(2000);
        solo.clickOnText("Only this time");
        solo.sleep(5000);
        solo.assertCurrentActivity("Wrong Activity", GeolocationActivity.class);

        solo.clickOnButton(0);
        solo.sleep(5000);

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Marker in Your Location"));
        marker.click();
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
