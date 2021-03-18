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

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Signup UI tests
 * Uses an firestore emulator
 * Tests if a user can access the signup screen and also signup
 */
public class SignUpTest {

    private Solo solo;
    private final String activityMessage = "Wrong Activity";

    //Feel free to change these to desired values, will affect your firebase collection
    private final String testName = "UserTestName";
    private final String testEmail = "email@test.com";
    private final String testPhone = "123-456-7890";
    public ActivityTestRule<MainActivity> rule;
    FirebaseFirestore db;

    @Rule
    public TestRule setRules(){
        Firestore.useEmulator();
        rule = new ActivityTestRule<>(MainActivity.class,true,true);
        return rule;
    }

    @Before
    public void setUp(){
        //Go to profileView from main
        db = Firestore.getInstance();

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

    }

    /**
     * Check if fragment occurs when taken username inputted when confirm pressed
     */
    @Test
    public void invalidUsernameTest(){
        //Set up taken username

        HashMap<String, Object> data = new HashMap<>();
        data.put("username", "UsernameTaken");
        data.put("email", "");
        data.put("phone", "");

        db.collection("Users").document("usernameTakenTester").set(data);


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

    }

    /**
     * Check if inputted values are displayed in profile when confirmed pressed
     */
    @Test
    public void signUpDisplayedTest(){
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

    }

    /**
     * Test if the sign up information is recorded in the database
     */
    @Test
    public void signUpDatabaseTest(){
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

        //Check if signed up values are in Database

        //Is username in database
        db.collection("Users").whereEqualTo("username", testName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // query successful
                    // check if empty (username not in use)
                    boolean usernameNotFound = task.getResult().isEmpty();
                    Log.d("TEST", "Task Success: usernameFound = " + usernameNotFound);
                    assertEquals(usernameNotFound,false);

                } else {
                    // query error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });

        //Is email in database
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

        //Is phone in database
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
    }

    @After
    public void tearDown(){
        //go back to main
        solo.goBack();
        solo.assertCurrentActivity(activityMessage,MainActivity.class);

        //Delete all users from Emulator
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
}
