package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private String username;
    private String email;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_information);

    }

    public void cancelPressed(View view){
        finish();
    }

    public void confirmPressed(View view){
        TextView input;
        boolean isValid=false;

        input = (TextView) findViewById(R.id.Email);
        email = input.getText().toString();

        input = (TextView) findViewById(R.id.Contact);
        phone = input.getText().toString();

        //Keep looping till valid username is inputted
        while(!isValid){
            input = (TextView) findViewById(R.id.Username);
            username = input.getText().toString();

            //check if username is in database
            //if not
            isValid=true;
        }


    }
}