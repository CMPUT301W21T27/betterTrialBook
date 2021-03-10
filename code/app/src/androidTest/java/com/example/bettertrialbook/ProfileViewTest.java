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
import static org.junit.Assert.assertNotEquals;

public class ProfileViewTest {

    private Solo solo;
    private final String activityMessage = "Wrong Activity";

    //Feel free to change these to desired values, will affect your firebase collection
    private final String testEmail = "editedEmail@testing.com";
    private final String testPhone = "555-1234";

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Test if ProfileViewActivity starts when profile icon tapped
     */
    @Test
    public void start(){
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(1);
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);
    }

    /**
     * Test if going back from Profile goes to Main
     */
    @Test
    public void returnToMain(){
        //Access profile view
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(1);
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //Return
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
    }

    /**
     * Test if fragment applies inputted changes to profileViewActivity
     */
    @Test
    public void EditContact(){
        //Access profile view
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(1);
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //Edit contact info
        solo.clickOnButton("Edit Contact Info");
        solo.enterText((EditText) solo.getView(R.id.email_editText),testEmail);
        solo.enterText((EditText) solo.getView(R.id.phone_editText),testPhone);
        solo.clickOnText("Apply");
        solo.waitForText(testPhone,1,2000);

        //Check email
        TextView display = (TextView) solo.getView(R.id.email_display);
        String email = display.getText().toString();
        assertEquals(testEmail,email);

        //Check phone
        display = (TextView) solo.getView(R.id.phone_display);
        String phone = display.getText().toString();
        assertEquals(testPhone,phone);

        solo.goBack(); //go back to main before ending, causes error otherwise
    }

    /**
     * Check if changes in fragment are NOT applied when Cancel clicked
     */
    @Test
    public void cancelEdit(){
        String testString = "Should Not Apply";

        //Access profile view
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(1);
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //Edit contact info
        solo.clickOnButton("Edit Contact Info");
        solo.enterText((EditText) solo.getView(R.id.email_editText),testString);
        solo.enterText((EditText) solo.getView(R.id.phone_editText),testString);
        solo.clickOnText("Cancel");

        //Check email
        TextView display = (TextView) solo.getView(R.id.email_display);
        String email = display.getText().toString();
        assertNotEquals(email,testString);

        //Check phone
        display = (TextView) solo.getView(R.id.phone_display);
        String phone = display.getText().toString();
        assertNotEquals(phone,testString);

        solo.goBack(); //go back to main before ending, causes error otherwise
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }

}
