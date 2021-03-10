package com.example.bettertrialbook.models;

public class Post {
    private String text;
    private String posterId;
    private String id;

    // default constructor for firebase
    public Post() {
    }

    public Post(String text, String posterId, String id) {
        this.text = text;
        this.posterId = posterId;
        this.id = id;
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

    @Override
    public String toString() {
        return "Post{" +
                "text='" + text + '\'' +
                ", posterId='" + posterId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
