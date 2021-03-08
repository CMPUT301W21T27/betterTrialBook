package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

import java.util.HashMap;

public class SignUp extends AppCompatActivity implements InvalidUsernameFragment.OnFragmentInteractionListener{

    private String username;
    private String email;
    private String phone;
    private User user;
    UserDAL uDAL = new UserDAL();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_information);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

    }

    public void cancelPressed(View view){
        endActivity();
    }

    public void confirmPressed(View view){
        TextView input;
        input = (TextView) findViewById(R.id.Email);
        email = input.getText().toString();

        input = (TextView) findViewById(R.id.Contact);
        phone = input.getText().toString();

        input = (TextView) findViewById(R.id.Username);
        username = input.getText().toString();

        if( username.equals("") || uDAL.userNameTaken(username) ){
            String message = "Username Taken";
            if(username.equals("")){
                message="Must Enter a Username";
            }
            new InvalidUsernameFragment(message).show(getSupportFragmentManager(), "ERROR_EXP");
            input.setText("");
            return;
        }

        user.setUsername(username);
        user.setContact(email,phone);

        uDAL.editUser(user);    //apply changes to database

        endActivity();
    }

    public void endActivity(){
        Intent intent = new Intent();
        intent.putExtra("User",user);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onOkPressed(){

    }

}