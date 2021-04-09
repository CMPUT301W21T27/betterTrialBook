package com.example.bettertrialbook.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A child class of Post used for Questions in the forum
 */
public class Question extends Post {
    private String title;
    private List<Reply> replies;
    // Used by firestore during automatic serialization
    protected String type = "question";

    public Question () {
        replies = new ArrayList<>();
    }

    /**
     * Gets the post's title
     * @return String of the post title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the post's title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the post's list of replies
     * @return List of replies for the question
     */
    public List<Reply> getReplies() {
        return replies;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", replies=" + replies +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", posterId='" + posterId + '\'' +
                ", id='" + id + '\'' +
                ", experimentId='" + experimentId + '\'' +
                '}';
    }

    /**
     * Gets the type of the post (question)
     * @return String of the post type
     */
    public String getType() {
        return type;
    }

    @Override
    public boolean validate() {
        if (! super.validate())
            return false;
        if (title == null) return false;
        return true;
    }

//    public void toMap() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("posterId", getPosterId());
//        map.put("experimentId", getExperimentId());
//    }

}
