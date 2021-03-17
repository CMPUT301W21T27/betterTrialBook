package com.example.bettertrialbook;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bettertrialbook.dal.Firestore;
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
import static org.junit.Assert.assertNotEquals;

public class ProfileViewTest {

    private Solo solo;
    private final String activityMessage = "Wrong Activity";
    private final String testEmail = "editedEmail@testing.com";
    private final String testPhone = "555-1234";

    FirebaseFirestore db;

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
    public void EditContactDisplayTest(){
        //Signup first
        signUp();

        //Access profile view
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
     * Test if editted contact info is recorded in database
     */
    @Test
    public void EditContactDatabaseTest(){
        //Signup first
        signUp();

        //Access profile view
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //Edit contact info
        solo.clickOnButton("Edit Contact Info");
        solo.enterText((EditText) solo.getView(R.id.email_editText),testEmail);
        solo.enterText((EditText) solo.getView(R.id.phone_editText),testPhone);
        solo.clickOnText("Apply");
        solo.waitForText(testPhone,1,2000);

        //Check email
        db.collection("Users").whereEqualTo("email", testEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // query successful
                    // check if empty (username not in use)
                    boolean emailNotFound = task.getResult().isEmpty();
                    Log.d("TEST", "Task Success: usernameFound = " + emailNotFound);
                    assertEquals(emailNotFound,false);

                } else {
                    // query error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });

        //Check phone
        db.collection("Users").whereEqualTo("phone", testPhone).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // query successful
                    // check if empty (username not in use)
                    boolean phoneNotFound = task.getResult().isEmpty();
                    Log.d("TEST", "Task Success: usernameFound = " + phoneNotFound);
                    assertEquals(phoneNotFound,false);

                } else {
                    // query error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });


        solo.goBack(); //go back to main before ending, causes error otherwise
    }

    /**
     * Check if changes in fragment are NOT applied when Cancel clicked
     */
    @Test
    public void cancelEditDatabase(){
        String testString = "Should Not Apply";

        //Signup
        signUp();

        //Access profile view
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //Edit contact info
        solo.clickOnButton("Edit Contact Info");
        solo.enterText((EditText) solo.getView(R.id.email_editText),testString);
        solo.enterText((EditText) solo.getView(R.id.phone_editText),testString);
        solo.clickOnText("Cancel");

        //Check email
        db.collection("Users").whereEqualTo("email", testString).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // query successful
                    // check if empty (username not in use)
                    boolean emailNotFound = task.getResult().isEmpty();
                    Log.d("TEST", "Task Success: usernameFound = " + emailNotFound);
                    assertEquals(emailNotFound,true);

                } else {
                    // query error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });

        //Check phone
        db.collection("Users").whereEqualTo("phone", testString).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // query successful
                    // check if empty (username not in use)
                    boolean phoneNotFound = task.getResult().isEmpty();
                    Log.d("TEST", "Task Success: usernameFound = " + phoneNotFound);
                    assertEquals(phoneNotFound,true);

                } else {
                    // query error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });


        solo.goBack(); //go back to main before ending, causes error otherwise
    }

    /**
     * Check that pressing "Cancel" does not change database
     */
    @Test
    public void cancelEditDisplay(){
        String testString = "Should Not Apply";

        //Signup
        signUp();

        //Access profile view
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

        solo.finishOpenedActivities();

    }

    public void signUp(){
        //Access profile view
        solo.assertCurrentActivity(activityMessage,MainActivity.class);
        solo.sleep(5000);   //wait for database access to finish
        solo.clickOnImage(1);
        solo.assertCurrentActivity(activityMessage,ProfileViewActivity.class);

        //Go to sign up screen
        solo.clickOnButton("Signup");
        solo.assertCurrentActivity(activityMessage,SignUp.class);
        solo.waitForText("Username:",1,2000);

        //Input taken username
        solo.enterText((EditText) solo.getView(R.id.Username),"EditTester");
        solo.clickOnButton("Confirm");
    }

}
