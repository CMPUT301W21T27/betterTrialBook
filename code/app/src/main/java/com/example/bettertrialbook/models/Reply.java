package com.example.bettertrialbook.models;

/**
 * A child class of Post used for answers in the forum
 */
public class Reply extends Post {
    private String questionId;
    // used by firestore during automatic serialization
    private String type = "reply";

    /**
     * Gets the question's id
     * @return String of the question id
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Sets the question's id
     * @param questionId
     */
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    /**
     * Gets the post's type (reply)
     * @return String of the post type
     */
    public String getType() {
        return type;
    }

    @Override
    public boolean validate() {
        if (!super.validate())
            return false;
        if (questionId == null) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "questionId='" + questionId + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", posterId='" + posterId + '\'' +
                ", id='" + id + '\'' +
                ", experimentId='" + experimentId + '\'' +
                '}';
    }


}
