package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class NonUserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        ImageView profilePic = findViewById(R.id.Profile);
        Button signUpButton = findViewById(R.id.SignUpButton);

        // Testing to go to the sign up page
        // Unfortunately, you cannot go back to the home screen v1
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goTOSignUpPage();
            }
        });
    }

    // Just a function and send to the sign up page
    public void goTOSignUpPage() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}