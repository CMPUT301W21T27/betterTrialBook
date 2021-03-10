package com.example.bettertrialbook;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/* To run:
* 1. Need to be using an ID that has not signed up yet
* 2. If your ID is already signed up, delete your collection from the database before running
* */
public class SignUpTest {

    private Solo solo;
    private final String activityMessage = "Wrong Activity";

    //Feel free to change these to desired values, will affect your firebase collection
    private final String testName = "UserTestName";
    private final String testEmail = "email@test.com";
    private final String testPhone = "123-456-7890";

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        //Go to profileView from main
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(1);
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);
    }

    /**
     * Test if clicking sign up button takes you to sign up screen
     */
    @Test
    public void start(){
        //See if signup starts
        solo.clickOnButton("Signup");
        solo.assertCurrentActivity(activityMessage,SignUp.class);
        solo.waitForText("Username:",1,2000);

        //Go back to profile
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //go back to main
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
    }

    /**
     * Check if pressing cancel returns to profile view
     */
    @Test
    public void cancelPressed(){
        //Go to sign up screen
        solo.clickOnButton("Signup");
        solo.assertCurrentActivity(activityMessage,SignUp.class);
        solo.waitForText("Username:",1,2000);

        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //go back to main
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
    }


    /**
     * Check if fragment occurs when no username inputted when confirm pressed
     */
    @Test
    public void emptyUsernameTest(){
        //Go to sign up screen
        solo.clickOnButton("Signup");
        solo.assertCurrentActivity(activityMessage,SignUp.class);
        solo.waitForText("Username:",1,2000);

        solo.clickOnButton("Confirm");
        solo.waitForText("Must Enter a Username");
        solo.clickOnText("OK");

        //Go back to profile
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //go back to main
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,MainActivity.class);

    }

    /**
     * Check if fragment occurs when taken username inputted when confirm pressed
     */
    @Test
    public void invalidUsernameTest(){
        //Go to sign up screen
        solo.clickOnButton("Signup");
        solo.assertCurrentActivity(activityMessage,SignUp.class);
        solo.waitForText("Username:",1,2000);

        //Input taken username
        solo.enterText((EditText) solo.getView(R.id.Username),"UsernameTaken");

        solo.clickOnButton("Confirm");
        solo.waitForText("Username Unavailable");
        solo.clickOnText("OK");

        //Go back to profile
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //go back to main
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
    }

    /**
     * Check if inputted values are displayed in profile when confirmed pressed
     */
    @Test
    public void signUpTest(){
        //Go to sign up screen
        solo.clickOnButton("Signup");
        solo.assertCurrentActivity(activityMessage,SignUp.class);
        solo.waitForText("Username:",1,2000);

        //Input taken username
        solo.enterText((EditText) solo.getView(R.id.Username),testName);
        solo.enterText((EditText) solo.getView(R.id.Email),testEmail);
        solo.enterText((EditText) solo.getView(R.id.Contact),testPhone);
        solo.clickOnButton("Confirm");

        //Make sure returned to profileView
        solo.waitForText("ID:",1,2000);
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //Check if inputted values in profile view
        TextView display = (TextView) solo.getView(R.id.username_display);
        String username = display.getText().toString();
        assertEquals(testName,username);

        display = (TextView) solo.getView(R.id.email_display);
        String email = display.getText().toString();
        assertEquals(testEmail,email);

        display = (TextView) solo.getView(R.id.phone_display);
        String phone = display.getText().toString();
        assertEquals(testPhone,phone);

        //go back to main
        solo.goBack();
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
