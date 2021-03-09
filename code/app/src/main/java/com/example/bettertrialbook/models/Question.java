package com.example.bettertrialbook.models;

public class Question extends Post {
    private String title;
    private String experimentId;

    public Question(String text, String posterId, String id, String title, String experimentId) {
        super(text, posterId, id);
        this.title = title;
        this.experimentId = experimentId;
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

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }
}
