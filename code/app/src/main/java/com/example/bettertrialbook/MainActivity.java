package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    UserDAL uDAL;
    User you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Spinner spinner = findViewById(R.id.SortMethod);
        ImageButton profile = (ImageButton) findViewById(R.id.Profile);

        // This is used to choose options for the sorting method
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.method, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        generateID();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void viewYourProfile(View view){
        //On opening app - generate a unique ID
        //Check if ID is already in database
        //If not, add to database with other fields as empty strings
        //create a user object to represent you with the fields from the database
        //send this user to profile activity if started
        //OR - don't make a user and pass only the ID around

        Intent intent = new Intent(this, ProfileViewActivity.class);
        intent.putExtra("User",you);
        startActivityForResult(intent,1);
    }

    public void generateID(){
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
            Log.d("TEST", "1. "+defaultIDValue);
        }

        uDAL = new UserDAL();
        // WORKS IN GENERAL BUT HAS SOME ISSUES WHEN MORE FEATURES ADDED
        uDAL.findUserByID(defaultIDValue, new UserDAL.FindUserByIDCallback() {
            @Override
            public void onCallback(User user) {
                // If no user found, create user
                if (user == null) {
                    //you = uDAL.addUser(defaultIDValue);
                } else {
                    Log.d("TEST", "4. "+user.getID()+user.getUsername());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        you = intent.getExtras().getParcelable("User");   //Accessing Parcelable Objects

    }

}
