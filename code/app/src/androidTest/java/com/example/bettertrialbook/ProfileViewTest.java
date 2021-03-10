package com.example.bettertrialbook;

import android.app.Fragment;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bettertrialbook.models.User;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProfileViewTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /* Test if clicking on icon opens up profileviewactivity */
    @Test
    public void start(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(0);
        solo.assertCurrentActivity("Wrong Activity",ProfileViewActivity.class);
        solo.waitForActivity(ProfileViewActivity.class);
    }

    @Test
    public void EditContact(){
        String testEmail = "nahmed2@gmail.com";
        String testPhone = "123-456-7890";
        //Access profile view
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(0);
        solo.assertCurrentActivity("Wrong Activity",ProfileViewActivity.class);

        //CRASHES HERE
        //Edit contact info
        solo.clickOnButton("Edit Contact Info");
        solo.enterText((EditText) solo.getView(R.id.email_editText),testEmail);
        solo.enterText((EditText) solo.getView(R.id.phone_editText),testPhone);
        solo.clickOnText("Apply");

        TextView display = (TextView) solo.getView(R.id.email_display);
        String email = display.getText().toString();

        display = (TextView) solo.getView(R.id.phone_display);
        String phone = display.getText().toString();

        assertEquals(testPhone,phone);

    }

    @After
    public void cleanUp(){
        solo.finishOpenedActivities();
    }
}
