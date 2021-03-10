package com.example.bettertrialbook.forum;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.ForumDAL;
import com.example.bettertrialbook.models.Question;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForumActivity extends AppCompatActivity {
    String expId = "3DPU8vG4aHcAJd7iHR7M";
    ArrayAdapter<Question> questionAdapter;
    CollectionReference collRef;
    ForumDAL forumDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        collRef = FirebaseFirestore.getInstance().collection("Questions");
        forumDAL = new ForumDAL();


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
}