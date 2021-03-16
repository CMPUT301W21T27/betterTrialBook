package com.example.bettertrialbook.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.ForumDAL;
import com.example.bettertrialbook.models.Question;

public class CreatePostActivity extends AppCompatActivity {
    private String expId;
    private String userId = "1234";

    ForumDAL forumDAL = new ForumDAL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        expId = getIntent().getStringExtra(Extras.EXPERIMENT_ID);
    }

    public void submit(View v) {
        EditText textInput = findViewById(R.id.create_post_text);
        EditText titleInput = findViewById(R.id.create_post_title);

        Question q = new Question();
        q.setTitle(titleInput.getText().toString());
        q.setText(textInput.getText().toString());
        q.setExperimentId(expId);
        q.setPosterId(userId);
        forumDAL.addPost(q, s -> {
            finish();
        });
    }

    public void cancel(View v) {
       finish();
    }
}