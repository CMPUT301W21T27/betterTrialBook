package com.example.bettertrialbook;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.experiment.ExperimentAddActivity;
import com.example.bettertrialbook.experiment.ExperimentViewActivity;
import com.example.bettertrialbook.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * UI testing for adding experiments
 */
public class AddExperimentTest {
    private Solo solo;
    FirebaseFirestore db;
    public ActivityTestRule<MainActivity> rule;

    @Rule
    public TestRule setRules(){
        Firestore.useEmulator();
        rule = new ActivityTestRule<>(MainActivity.class,true,true);
        return rule;
    }
    @Before
    public void setUp() throws Exception {
        db = Firestore.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Test if pressing the CREATE button properly brings you to the ExperimentAddActivity Activity
     */
    @Test
    public void start() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(5000);
        solo.clickOnButton("Create");

        // Check to make sure that the class switched
        solo.assertCurrentActivity("Wrong Activity", ExperimentAddActivity.class);
        solo.waitForText("New Experiment", 1, 2000);
    }

    /**
     * Test if pressing the 'Cancel' button properly returns you to main activity
     */
    @Test
    public void cancelButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(5000);
        solo.clickOnButton("Create");

        // Check to make sure that the class switched
        solo.assertCurrentActivity("Wrong Activity", ExperimentAddActivity.class);
        solo.waitForText("New Experiment", 1, 2000);

        // Check to make sure it returns properly
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Test to see if trying to publish without filling in all queries keeps you on publish screen.
     */
    @Test
    public void publishFail() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(5000);
        solo.clickOnButton("Create");

        // Check to make sure that the class switched
        solo.assertCurrentActivity("Wrong Activity", ExperimentAddActivity.class);
        solo.waitForText("New Experiment", 1, 2000);

        // Check to make sure that publish does not return if everything is not filled out
        solo.clickOnButton("Publish");
        solo.assertCurrentActivity("Wrong Activity", ExperimentAddActivity.class);
    }

    /**
     * Test if text properly displays when entering in, and publishing sends you to the ExperimentViewActivity activity.
     * Checks if all the correct data appears as intended.
     */
    @Test
    public void enterData() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(5000);
        solo.clickOnButton("Create");

        // Check to make sure that the class switched
        solo.assertCurrentActivity("Wrong Activity", ExperimentAddActivity.class);
        solo.waitForText("New Experiment", 1, 2000);

        // Test the Type Spinner
        assertTrue(solo.searchText("Count-Based"));
        solo.pressSpinnerItem(0, 1);
        assertTrue(solo.searchText("Binomial"));

        // Fill out all places and check to see if it is properly displayed
        solo.enterText((EditText) solo.getView(R.id.description_editText), "Counting Stars");
        solo.waitForText("Counting Stars", 1, 2000);

        solo.enterText((EditText) solo.getView(R.id.mintrials_editText), "200");
        solo.waitForText("200", 1, 2000);

        solo.enterText((EditText) solo.getView(R.id.region_editText), "Calgary");
        solo.waitForText("Calgary", 1, 2000);

        // Check if pressing the Geo-Location button properly changes the setting
        solo.clickOnButton("No");
        solo.waitForText("Yes", 1, 2000);

        // Check if pressing the 'Publish' button returns to main activity
        solo.clickOnButton("Publish");
        solo.assertCurrentActivity("Wrong Activity", ExperimentViewActivity.class);

        // Check if all the data appears correctly
        ExperimentViewActivity activity = (ExperimentViewActivity) solo.getCurrentActivity();
        final String descriptionText = activity.descriptionText.getText().toString();
        assertEquals("Description: Counting Stars", descriptionText);
        final String regionText = activity.regionText.getText().toString();
        assertEquals("Region: Calgary", regionText);

        solo.sleep(5000);
    }

    /**
     * Close activities after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
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
