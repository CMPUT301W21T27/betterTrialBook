package com.example.bettertrialbook.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

/*
* Sign up activity
* Takes user input for username and contact info
* Users must enter a unique username, contact info optional
* Can cancel signup or confirm
* */
public class SignUp extends AppCompatActivity implements InvalidUsernameFragment.OnFragmentInteractionListener {

    private String username;
    private String email;
    private String phone;
    UserDAL uDAL = new UserDAL();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_information);
    }

    public void cancelPressed(View view){
        endActivity();
    }

    public void confirmPressed(View view){
        TextView input;

        //Get inputted values
        input = (TextView) findViewById(R.id.Email);
        email = input.getText().toString();

        input = (TextView) findViewById(R.id.Contact);
        phone = input.getText().toString();

        input = (TextView) findViewById(R.id.Username);
        username = input.getText().toString();

        //Validate username
        if( username.equals("") ){
            String message ="Must Enter a Username";
            new InvalidUsernameFragment(message).show(getSupportFragmentManager(), "ERROR_EXP");
            input.setText("");

        }else{
            //Using callbacks like in main
            uDAL.userNameTaken(username, new UserDAL.UsernameTakenCallback() {
                @Override
                public void onCallback(boolean isNotTaken) {
                    Log.d("TEST","Callback Success: isNotTaken = "+isNotTaken);
                    if(!isNotTaken ){
                        String message ="Username Unavailable";
                        new InvalidUsernameFragment(message).show(getSupportFragmentManager(), "ERROR_EXP");
                    }else{
                        You.getUser().setUsername(username);
                        You.getUser().setContact(email,phone);

                        uDAL.editUser(You.getUser());    //apply changes to database
                        endActivity();
                    }
                }
            });
        }
    }

    public void endActivity(){
        //Ends the activity
        setResult(Activity.RESULT_OK,getIntent());
        finish();
    }

    @Override
    public void onOkPressed(){
        //Ok button on fragment
    }

    @Override
    public void onBackPressed() {
        endActivity();
        super.onBackPressed();
    }

}