package com.example.bettertrialbook.models;

import java.util.List;

public class Question extends Post {
    private String id;
    private String title;
    private String experimentId;
    private List<String> replyIds;

    // default constructor required so FireStore can automatically inflate documents into objects
    public Question() {
    }

    public Question(String text, String posterId, String id, String title, String experimentId, List<String> replyIds) {
        super(text, posterId, id);
        this.title = title;
        this.experimentId = experimentId;
        this.replyIds = replyIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public List<String> getReplyIds() {
        return this.replyIds;
    }

    public void setReplyIds(List<String> replyIds) {
        this.replyIds = replyIds;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", experimentId='" + experimentId + '\'' +
                ", replyIds=" + replyIds +
                '}';
    }
}
