package com.example.bettertrialbook.forum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.Firestore;
import com.example.bettertrialbook.dal.ForumDAL;
import com.example.bettertrialbook.models.Post;
import com.example.bettertrialbook.models.Question;
import com.google.firebase.firestore.CollectionReference;


public class ForumActivity extends AppCompatActivity {
    String expId;
    ArrayAdapter<Question> questionAdapter;
    CollectionReference collRef;
    ForumDAL forumDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        expId = getIntent().getStringExtra(Extras.EXPERIMENT_ID);
        collRef = Firestore.getInstance().collection("Questions");
        forumDAL = new ForumDAL();

        setTitle();
        questionAdapter = new QuestionList(this);
        ListView questionList = findViewById(R.id.question_list);
        questionList.setAdapter(questionAdapter);

        setupSnapshotListener();
    }

    private void setupSnapshotListener() {
        forumDAL.subscribeToQuestions(expId, questions -> {
            questionAdapter.clear();
            questionAdapter.addAll(questions);
        });
    }

    public void openCreateQuestionActivity(View v) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(Extras.EXPERIMENT_ID, expId);
        startActivity(intent);
    }

    private void setTitle() {
        TextView title = findViewById(R.id.forum_experiment_title);
        title.setText(expId);
    }
}