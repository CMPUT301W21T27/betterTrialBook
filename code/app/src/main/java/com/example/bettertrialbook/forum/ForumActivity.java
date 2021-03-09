package com.example.bettertrialbook.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Question;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {
    String expId = "3DPU8vG4aHcAJd7iHR7M";
    ArrayAdapter<Question> questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        CollectionReference collRef = FirebaseFirestore.getInstance().collection("Questions");

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("text", "user", "id", "title", "2734"));

        questionAdapter = new QuestionList(this, questions);
        ListView questionList = findViewById(R.id.question_list);
        questionList.setAdapter(questionAdapter);
    }
}