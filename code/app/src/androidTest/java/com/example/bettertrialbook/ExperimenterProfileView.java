package com.example.bettertrialbook;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.experiment.ExperimentViewActivity;
import com.example.bettertrialbook.home.MainActivity;
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

import static org.junit.Assert.assertEquals;

/**
 * UI Testing for statistics feature
 */
public class ExperimenterProfileView {
    private Solo solo;
    private final String activityMessage = "Wrong Activity";
    FirebaseFirestore db;
    String experimentName = "Profile View Test";

    public ActivityTestRule<MainActivity> rule;

    @Rule
    public TestRule setRules(){
        Firestore.useEmulator();
        rule = new ActivityTestRule<>(MainActivity.class,true,true);
        return rule;
    }
    @Before
    public void setUp(){
        db = Firestore.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
    }

    @Test
    public void openYourProfile(){
        createExperiment(You.getUser().getID());

        solo.clickOnImage(0);
        solo.enterText(0,experimentName);
        solo.waitForText(experimentName);

        solo.clickInList(0);
        solo.assertCurrentActivity(activityMessage,ExperimentViewActivity.class);

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.ownerId_text));

        solo.assertCurrentActivity(activityMessage, ProfileViewActivity.class);

        View view = solo.getCurrentActivity().findViewById(R.id.button2);
        assertEquals(view.getVisibility(),View.VISIBLE);

    }

    @Test
    public void viewOthersProfile(){
        createUser();
        createExperiment("NotYou");

        solo.clickOnImage(0);
        solo.enterText(0,experimentName);
        solo.waitForText(experimentName);

        solo.clickInList(0);
        solo.assertCurrentActivity(activityMessage,ExperimentViewActivity.class);

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.ownerId_text));

        solo.assertCurrentActivity(activityMessage, ProfileViewActivity.class);

        View view = solo.getCurrentActivity().findViewById(R.id.button2);
        assertEquals(view.getVisibility(),View.INVISIBLE);
    }

    public void createExperiment(String id) {
        CollectionReference colRef = db.collection("Experiments");

        Map<String, Object> data = new HashMap<>();
        data.put("ActiveStatus", "Publish");
        data.put("Description", "Profile View Test");
        data.put("GeoLocationRequired", false);
        data.put("MinTrials", 200);
        data.put("Owner", id);
        data.put("PublishStatus", "Active");
        data.put("Region", "Edmonton");
        data.put("TrialType", "Count-Based");

        colRef.add(data);
        solo.sleep(5000);
    }

    public void createUser(){
        CollectionReference colRef = db.collection("Users");

        Map<String, String> data = new HashMap<>();
        data.put("email", "notYou@email.com");
        data.put("phone", "555-1234");
        data.put("username", "iAmNotYou");

        colRef.document("NotYou").set(data);
        solo.sleep(5000);
    }


    @After
    public void tearDown(){
        //Delete users from emulator
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

        solo.finishOpenedActivities();

    }
}
