package com.example.bettertrialbook.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.ForumDAL;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.Post;
import com.example.bettertrialbook.models.Question;
import com.example.bettertrialbook.models.Reply;

/**
 * An activity used to create a Question or a Reply and post it to Firestore.
 * By default, this creates a Question.
 * If a question is passed in the intent as an extra, then this will construct a reply to the given question.
 */
public class CreatePostActivity extends AppCompatActivity {
    private String expId;
    private String userId;
    private Question originalQuestion;
    private UserDAL userDAL = new UserDAL();
    ForumDAL forumDAL = new ForumDAL();
    private Class type = Question.class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Intent intent = getIntent();
        expId = intent.getStringExtra(Extras.EXPERIMENT_ID);
        userId = userDAL.getDeviceUserId(this);

        // if a question is specified, this will create a reply
        if (intent.hasExtra(Extras.QUESTION)) {
            originalQuestion = (Question) intent.getSerializableExtra(Extras.QUESTION);
            type = Reply.class;
            changeLayoutForReply();
        }
    }

    /**
     *
     * @param v Parameter needed to use this as onClick xml file
     */
    public void submit(View v) {
        EditText textInput = findViewById(R.id.create_post_text);
        EditText titleInput = findViewById(R.id.create_post_title);

        Post post = initializePost();
        post.setText(textInput.getText().toString());
        // only questions have titles
        if (post instanceof Question) {
            ((Question) post).setTitle(titleInput.getText().toString());
        } else {
            ((Reply) post).setQuestionId(originalQuestion.getId());
        }
        forumDAL.addPost(post, s -> {
            finish();
        });
    }

    /**
     * Initializes a post object and sets all fields that don't depend on user input.
     * @return
     */
    private Post initializePost() {
        Post post = type == Question.class ? new Question() : new Reply();
        post.setExperimentId(expId);
        post.setPosterId(userId);
        return post;
    }

    /**
     * Makes the original question visible on the screen and removes the title EditText
     */
    private void changeLayoutForReply() {
        TextView title = findViewById(R.id.create_post_page_title);
        title.setText("Post Reply");
        // Fill in the title/text of the question we're replying to
        TextView originalTitle = findViewById(R.id.create_post_original_title);
        originalTitle.setText(originalQuestion.getTitle());
        originalTitle.setVisibility(View.VISIBLE);
        TextView originalText = findViewById(R.id.create_post_original_text);
        originalText.setText(originalQuestion.getText());
        originalText.setVisibility(View.VISIBLE);

        // removes the title input
        EditText titleInput = findViewById(R.id.create_post_title);
        titleInput.setVisibility(View.GONE);
    }

    public void cancel(View v) {
        finish();
    }
}