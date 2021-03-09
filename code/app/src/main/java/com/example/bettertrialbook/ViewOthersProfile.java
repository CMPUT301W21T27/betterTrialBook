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
        display.setText(user.getID());
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
