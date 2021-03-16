package com.example.bettertrialbook.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Question extends Post {
    private String title;
    private List<Reply> replies;
    // Used by firestore during automatic serialization
    private String type = "question";

    public Question () {
        replies = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Reply> getReplies() {
        return replies;
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
