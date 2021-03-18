package com.example.bettertrialbook;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddExperimentTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Test if pressing the CREATE button properly brings you to the ExperimentAddActivity Activity
     */
    @Test
    public void start() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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
    }

    /**
     * Close activities after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
