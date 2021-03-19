/*
Contains the Post class which is used in the Q&A forums
 */

package com.example.bettertrialbook.models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Post implements Serializable {
    protected String text;
    protected String posterId;
    protected String id;
    protected String experimentId;

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

    /**
     * Checks if this post's id equals another post.
     * Firestore guarantees that ids will be unique.
     * Does NOT check if any of the posts' content match.
     * @param other
     * @return true if ids match
     */
    public boolean equals(@Nullable Post other) {
        if (other == null)
            return false;
        return getId().equals(other.id);
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
