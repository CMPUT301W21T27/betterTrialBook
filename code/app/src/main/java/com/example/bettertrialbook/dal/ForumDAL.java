package com.example.bettertrialbook.dal;

import android.util.Log;

import com.example.bettertrialbook.models.Question;
import com.example.bettertrialbook.models.Reply;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumDAL {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collRef = db.collection("Posts");

    public void subscribeToQuestions(String expId, Callback<List<Question>> callback) {
        collRef.whereEqualTo("experimentId", expId).addSnapshotListener((querySnapshot, error) -> {
            List<Question> questions = serializeQuestions(querySnapshot);
            callback.execute(questions);
        });
    }


    public void updateQuestion(String qId, Question question) {
        collRef.document(qId).set(question);
    }

    private List<Question> serializeQuestions(QuerySnapshot querySnapshot) {
        // maps question id to corresponding question
        Map<String, Question> questionMap = new HashMap<>();
        // maps question id to list of its replies
        Map<String, ArrayList<Reply>> replyListMap = new HashMap<>();
        List<Reply> replies = new ArrayList<>();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            if (doc.get("type") == "question") {
                Question q = doc.toObject(Question.class);
                q.setId(doc.getId());
                questionMap.put(q.getId(), q);
            } else if (doc.get("type") == "reply") {
                Reply r = doc.toObject(Reply.class);
                r.setId(r.getId());
                replies.add(r);
            } else {
                Log.d("ForumDAL", doc.toString());
            }
        }
        for (Reply r : replies) {
            Question q = questionMap.get(r.getQuestionId());
            q.getReplies().add(r);
        }
        return new ArrayList(questionMap.values());
    }

}
