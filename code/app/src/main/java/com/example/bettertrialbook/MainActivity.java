package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    UserDAL uDAL;
    User you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateID();
    }

    public void viewYourProfile(View view) {
        /*Calls the ProfileViewActivity
         * Sends user object "you" to display their info
         * Expects an updated "you" object as a return
         * */
        Intent intent = new Intent(this, ProfileViewActivity.class);
        intent.putExtra("User", you);
        startActivityForResult(intent, 1);
    }

    public void generateID() {
        /*
         * Generates a unqiue ID per user
         * Checks if ID is in database
         *   Adds ID to DB if it's not
         * Creates user object "you" to represent you
         * */

        // Generating ID
        // https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values
        SharedPreferences sharedPref = this.getSharedPreferences("uniqueID", Context.MODE_PRIVATE);
        String defaultIDValue = sharedPref.getString("uniqueID", null);
        if (defaultIDValue == null) {
            Log.d("Generate", "Empty id");
            String uID = UUID.randomUUID().toString();
            Log.d("Generate", uID.toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("uniqueID", uID);
            editor.apply();
            defaultIDValue = uID;

        } else {
            Log.d("TEST", "1. " + defaultIDValue);
        }

        // Checking if ID in Database, else adds it to database
        final String finalID = defaultIDValue;
        uDAL = new UserDAL();

        // https://www.youtube.com/watch?v=0ofkvm97i0s - Callback
        uDAL.findUserByID(defaultIDValue, new UserDAL.FindUserByIDCallback() {
            @Override
            public void onCallback(User user) {
                // If no user found, create user
                if (user == null) {
                    you = uDAL.addUser(finalID);
                } else {
                    Log.d("TEST", "4. " + user.getID() + user.getUsername());
                    you = user;
                }
            }
        });
    }

    //Return from ProfileViewActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        you = intent.getExtras().getParcelable("User");   //Accessing Parcelable Objects

    }

    public void createExperiment(View view) {
        Intent intent = new Intent(this, ExperimentAddActivity.class);
        startActivity(intent);
    }

}
