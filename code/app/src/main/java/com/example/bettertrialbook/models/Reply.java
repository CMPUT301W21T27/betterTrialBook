package com.example.bettertrialbook.models;

public class Reply extends Post {
    private String questionId;
    // used by firestore during automatic serialization
    private String type = "reply";

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

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
