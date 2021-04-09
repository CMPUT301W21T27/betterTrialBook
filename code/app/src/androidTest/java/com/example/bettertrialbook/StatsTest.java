package com.example.bettertrialbook;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.experiment.ExperimentViewActivity;
import com.example.bettertrialbook.home.MainActivity;
import com.example.bettertrialbook.profile.ProfileViewActivity;
import com.example.bettertrialbook.statistic.Histogram;
import com.example.bettertrialbook.statistic.LineGraph;
import com.example.bettertrialbook.statistic.StatsNumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.math.Stats;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * UI tests for the View Stats option
 * Runs using the Firebase emulator
 */
public class StatsTest {
    private Solo solo;
    private final String activityMessage = "Wrong Activity";
    FirebaseFirestore db;
    String experimentName = "Stats Test";
    String experimenterName = "StatsTester";

    public ActivityTestRule<MainActivity> rule;

    @Rule
    public TestRule setRules(){
        Firestore.useEmulator();
        rule = new ActivityTestRule<>(MainActivity.class,true,true);
        return rule;
    }

    /**
     * Creates a generic user and a generic experiment made by them
     * Searches for the experiment and opens it up and subscribes
     */
    @Before
    public void setUp(){
        db = Firestore.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
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
     * Tests if the correct activity opens when "View Stats" is clicked
     */
    @Test
    public void statsOpensTest(){
        //Test if the correct activity opens
        solo.clickOnButton("View Stats");
        solo.assertCurrentActivity(activityMessage, StatsNumber.class);
        solo.sleep(2000);
    }

    /**
     * Tests if the displayed statistics are correct
     */
    @Test
    public void checkStatsTest(){
        //Add some trials to create stats for
        addTrials(true);

        //Open up view stats activity
        solo.clickOnButton("View Stats");
        solo.assertCurrentActivity(activityMessage, StatsNumber.class);

        //Check if the displayed stats are correct
        //Stats were calculated using: https://www.calculator.net/statistics-calculator.html and https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php
        //Median = 35.0, Std. Dev = 13.336, Mean = 33.4, Q1 = 20.5, Q3 = 45.5;

        TextView display;
        String result;

        //Check if Median is correct
        display = (TextView) solo.getView(R.id.Median_Result);
        result = display.getText().toString();
        assertEquals(result,"35.0");

        //Check if Standard Deviation is correct
        display = (TextView) solo.getView(R.id.StdDev_Result);
        result = display.getText().toString();
        assertEquals(result,"13.335666");

        //Check if Mean is correct
        display = (TextView) solo.getView(R.id.Mean_Result);
        result = display.getText().toString();
        assertEquals(result,"33.4");

        //Check if First Quartile is correct
        display = (TextView) solo.getView(R.id.FirstQuartile_Result);
        result = display.getText().toString();
        assertEquals(result,"20.5");

        //Check if Third Quartile is correct
        display = (TextView) solo.getView(R.id.ThirdQuartile_Result);
        result = display.getText().toString();
        assertEquals(result,"45.5");
    }

    /**
     * Tests if the correct activity opens when the Histogram icon is clicked
     */
    @Test
    public void histogramOpensTest(){
        //Add some trials to generate the graphs
        addTrials(false);

        //Open up View Stats
        solo.clickOnButton("View Stats");
        solo.assertCurrentActivity(activityMessage, StatsNumber.class);
        solo.sleep(2000);

        //Open Histogram
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.Histogram));
        solo.assertCurrentActivity(activityMessage, Histogram.class);
        solo.sleep(2000);
    }

    /**
     * Tests if the correct activity opens when the Plot Over Time icon is clicked
     */
    @Test
    public void linePlotOpensTest(){
        //Add some trials to generate the graphs
        addTrials(false);

        //Open up View Stats
        solo.clickOnButton("View Stats");
        solo.assertCurrentActivity(activityMessage, StatsNumber.class);
        solo.sleep(2000);

        //Open Line Plots
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.PlotOverTime));
        solo.assertCurrentActivity(activityMessage, LineGraph.class);
        solo.sleep(2000);
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
        data.put("GeoLocationRequired", false);
        data.put("MinTrials", 200);
        data.put("Owner", experimenterName);
        data.put("PublishStatus", "Active");
        data.put("Region", "Edmonton");
        data.put("TrialType", "Non-Negative Integer");

        colRef.document("StatsExperiment").set(data);
        solo.sleep(5000);
    }

    /**
     * @param multiTrials Adds a single trial (multiTrials = false) or 5 trials (multiTrials=true)
     */
    public void addTrials(boolean multiTrials){
        //Populate trials with these values
        List<String> list = Arrays.asList("41","35","10","50","31");
        for(int i=0;i<5;i++){
            solo.clickOnButton("Add Trial");
            solo.waitForText("Non-Negative Integer");
            solo.enterText((EditText) solo.getView(R.id.intamount_editText),list.get(i));
            solo.clickOnText("OK");

            //Add only one trial
            if(!multiTrials){
                break;
            }
        }
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
