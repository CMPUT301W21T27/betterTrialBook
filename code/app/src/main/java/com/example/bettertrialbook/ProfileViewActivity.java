package com.example.bettertrialbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

public class ProfileViewActivity extends AppCompatActivity implements EditContactFragment.OnFragmentInteractionListener {

    User user;
    UserDAL uDAL = new UserDAL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        displayInformation();
    }

    public void setUser(User setTo){
        user=setTo;
    }

    public void displayInformation(){
        TextView display;
        Button button;

        display = (TextView) findViewById(R.id.userID_display);
        display.setText(Integer.toString(user.getID()));
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
        Intent intent = new Intent(this, SignUp.class);
        intent.putExtra("User",user);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        User user = intent.getExtras().getParcelable("User");   //Accessing Parcelable Objects

        //Check if user has a username, else sign up was canceled
        if(user.getUsername().equals("")){
            return;
        }

        Button button = (Button) findViewById(R.id.button2);
        button.setText("Edit Contact Info");

        setUser(user);
        displayInformation();
    }

    public void userEditInfo(){
        new EditContactFragment().show(getSupportFragmentManager(), "EDIT_EXP");
    }

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
     *   Prepares Experiment object to send back to Main
     *   Returns to main */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* Get return object ready  */
        Intent intent = new Intent();
        intent.putExtra("User",user);
        setResult(Activity.RESULT_OK,intent);

        this.finish();
        return true;
    }




}

