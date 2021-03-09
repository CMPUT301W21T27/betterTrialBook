package com.example.bettertrialbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.models.User;

/*
* Like viewing your own profile, but no signup/edit button
* */
public class ViewOthersProfile extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_others);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        displayInformation();
    }

    public void displayInformation(){
        TextView display;

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

    }

    /* BACK BUTTON
     *   Returns to experiment view */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        return true;
    }
    
}
