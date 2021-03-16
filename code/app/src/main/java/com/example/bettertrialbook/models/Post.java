package com.example.bettertrialbook.models;

public abstract class Post {
    private String text;
    private String posterId;
    private String id;
    private String experimentId;

    public void setText(String text) {
        this.text = text;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getText() {
        return text;
    }

    public String getPosterId() {
        return posterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Checks if all required fields have been set.
     */
    public boolean validate() {
        if (text == null) return false;
        if (posterId == null) return false;
        if (experimentId == null) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Post{" +
                "text='" + text + '\'' +
                ", posterId='" + posterId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
