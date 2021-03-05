package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    int uniqueID;
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
        uniqueID = 1234;
        uDAL = new UserDAL();

        you = uDAL.findUserByID(uniqueID);

        //If no user found, create user
        if (you==null){
            you = uDAL.addUser(uniqueID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        you = intent.getExtras().getParcelable("User");   //Accessing Parcelable Objects

    }

}
