package com.example.bettertrialbook.dal;

import com.example.bettertrialbook.models.Question;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ForumDAL {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collRef = db.collection("Questions");

    public void subscribeToQuestions(String expId, Callback<List<Question>> callback) {
        collRef.whereEqualTo("experimentId", expId).addSnapshotListener((value, error) -> {
            List<Question> questions = new ArrayList<>();
            for (DocumentSnapshot doc: value.getDocuments()) {
                Question q = doc.toObject(Question.class);
                q.setId(doc.getId());
                questions.add(q);
            }
            callback.execute(questions);
        });
    }


    public void updateQuestion(String qId, Question question) {
        collRef.document(qId).set(question);
    }

    public interface Callback<T> {
        void execute(T t);
    }
}
