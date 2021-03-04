package com.example.bettertrialbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileViewActivity extends AppCompatActivity {

    int ID; //send you're user ID if calling this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Intent intent = getIntent();

        //If user in database, replace sign up with edit contact info
        ID = intent.getIntExtra("userID",0);
    }

    public void displayInformation(){
        TextView display;

        display = (TextView) findViewById(R.id.userID_display);
        display.setText(ID);

        //if in database, populate with database information

    }

    public void buttonClicked(View view){
        //Check if button says signup or edit
        Button button = (Button) findViewById(R.id.button2);

        String buttonName = button.getText().toString();

        if(buttonName.equals("Signup")){
            userSignUp();
        }
        else{
            //userEditInfo()
        }
    }

    public void userSignUp(){
        Intent intent = new Intent(this, ProfileViewActivity.class);
        startActivity(intent);
        //Check if user in database, otherwise user pressed cancel
        //If user in database, change signup to say edit

    }

    public void userEditInfo(){

    }
}

