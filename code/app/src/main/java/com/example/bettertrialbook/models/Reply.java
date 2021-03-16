package com.example.bettertrialbook.models;

public class Reply extends Post{
    private String questionId;
    // used by firestore during automatic serialization
    private String type = "reply";

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean validate() {
        if( !super.validate())
            return false;
        if (questionId == null) return false;
        return true;
    }
}
