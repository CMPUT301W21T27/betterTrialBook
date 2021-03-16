package com.example.bettertrialbook.models;

import java.util.ArrayList;
import java.util.List;

public class Question extends Post {
    private String title;
    private List<Reply> replies;

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

}
