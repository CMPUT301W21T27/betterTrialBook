package com.example.bettertrialbook.dal;

import android.util.Log;

import com.example.bettertrialbook.models.Post;
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

import javax.annotation.Nullable;

/**
 * A DAL for handling any forum interactions with the database.
 */
public class ForumDAL {
    FirebaseFirestore db = Firestore.getInstance();
    CollectionReference collRef = db.collection("Posts");

    public void subscribeToQuestions(String expId, Callback<List<Question>> callback) {
        collRef.whereEqualTo("experimentId", expId).addSnapshotListener((querySnapshot, error) -> {
            List<Question> questions = deserializeQuestions(querySnapshot);
            callback.execute(questions);
        });
    }

    /**
     * Adds the given post to Firestore and calls the onSuccess callback with it's new id.
     *
     * @param post      A question or reply
     * @param onSuccess Callback that gets passed the post's id
     * @throws IllegalArgumentException
     */
    public void addPost(Post post, @Nullable Callback<String> onSuccess) {
        if (!post.validate()) {
            throw new IllegalArgumentException("Post does not have all necessary fields set. \n" + post.toString());
        }
        collRef.add(post).addOnSuccessListener(doc -> {
            String qId = doc.getId();
            if (onSuccess != null)
                onSuccess.execute(qId);
        });
    }

    /**
     * Updates a question in the database
     * @param qId
     * @param question
     */
    public void updateQuestion(String qId, Question question) {
        collRef.document(qId).set(question);
    }

    private List<Question> deserializeQuestions(QuerySnapshot querySnapshot) {
        // maps question id to corresponding question
        Map<String, Question> questionMap = new HashMap<>();
        List<Reply> replies = new ArrayList<>();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            if (doc.get("type") == null)
                continue;

            String type = doc.get("type").toString();
            if (type.equals("question")) {
                Question q = doc.toObject(Question.class);
                q.setId(doc.getId());
                questionMap.put(q.getId(), q);
            } else if (type.equals("reply")) {
                Reply r = doc.toObject(Reply.class);
                r.setId(doc.getId());
                replies.add(r);
            } else {
                Log.d("ForumDAL", doc.toString());
                Log.d("ForumDAL", doc.get("type").toString());
            }
        }
        for (Reply r : replies) {
            Question q = questionMap.get(r.getQuestionId());
            q.getReplies().add(r);
        }
        return new ArrayList(questionMap.values());
    }

}
