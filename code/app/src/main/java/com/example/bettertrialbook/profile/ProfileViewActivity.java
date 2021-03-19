package com.example.bettertrialbook.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

/**
* Activity to view your profile
* Has either a signup button or an edit contact button
* Displays all your info, ID, username, contact info
* Signup - starts signup activity
* Edit - calls edit contact fragment
* Applies your sign up or edit to the database
* */
public class ProfileViewActivity extends AppCompatActivity implements EditContactFragment.OnFragmentInteractionListener {

    private User user;
    private UserDAL uDAL = new UserDAL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        //Get user to display
        user =  getIntent().getParcelableExtra("User");

        //Initialize back arrow button to return to main
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Display user information
        displayInformation();
    }

    public void setUser(User setTo){
        //Displays all information in User object
        user=setTo;
    }

    public void displayInformation(){
        //Displays information
        //Sets name of button depending on if user signed up or not

        TextView display;
        Button button;

        //Display only 8 chars of ID
        display = (TextView) findViewById(R.id.userID_display);
        if(user.getID().length()<8){
            display.setText(user.getID());
        }else{
            display.setText(user.getID().substring(0,8));
        }
        display.invalidate();

        display = (TextView) findViewById(R.id.username_display);
        display.setText(user.getUsername());
        display.invalidate();

        display = (TextView) findViewById(R.id.email_display);
        display.setText(user.getContact().getEmail());
        display.invalidate();

        display = (TextView) findViewById(R.id.phone_display);
        display.setText(user.getContact().getPhone());
        display.invalidate();

        //If user signed up, change button name
        if (!user.getUsername().equals("")){
            button = (Button) findViewById(R.id.button2);
            button.setText("Edit Contact Info");
            button.invalidate();
        }
    }

    public void buttonClicked(View view){
        //Check if button says signup or edit
        Button button = (Button) findViewById(R.id.button2);

        String buttonName = button.getText().toString();

        if(buttonName.equals("Signup")){
            userSignUp();
        }
        else{
            userEditInfo();
        }
    }

    public void userSignUp(){
        // Calls sign up activity
        // Sends user object to sign up to update
        // Expects updated user object as return
        Intent intent = new Intent(this, SignUp.class);
        intent.putExtra("User",user);
        startActivityForResult(intent,1);

    }

    public void endActivity(){
        Intent intent = new Intent();
        intent.putExtra("User",user);
        setResult(Activity.RESULT_OK,intent);

        this.finish();
    }

    // Return from Signup Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        User user = intent.getExtras().getParcelable("User");   //Accessing Parcelable Objects

        // Check if user has a username, else sign up was canceled
        if(user.getUsername().equals("")){
            return;
        }

        // If user did sign up, change button name
        Button button = (Button) findViewById(R.id.button2);
        button.setText("Edit Contact Info");

        // Set user to edited user object
        // Display updated information
        setUser(user);
        displayInformation();
    }

    // Calls edit fragment
    public void userEditInfo(){
        String email = user.getContact().getEmail();
        String phone = user.getContact().getPhone();
        new EditContactFragment(email, phone).show(getSupportFragmentManager(), "EDIT_EXP");
    }

    // Edit contact fragment results
    @Override
    public void onOkPressed(String email, String phone){
        if(!email.equals("")){
            user.getContact().setEmail(email);
        }
        if(!phone.equals("")){
            user.getContact().setPhone(phone);
        }

        uDAL.editContactInfo(user);

        displayInformation();

    }

    /* BACK BUTTON
     *   Prepares user object to send back to Main
     *   Returns to main */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* Get return object ready  */
        endActivity();
        return true;
    }

    @Override
    public void onBackPressed() {
        endActivity();
        super.onBackPressed();
    }

}

