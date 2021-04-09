package com.example.bettertrialbook.models;

import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * Contains the Post class which is used in the Q&A forums
 */
public class Post implements Serializable {
    protected String text;
    protected String posterId;
    protected String id;
    protected String experimentId;

    /**
     * Sets the text of the post
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets the poster id of the post
     * @param posterId
     */
    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    /**
     * Gets the post's experiment id
     * @return String of experiment id
     */
    public String getExperimentId() {
        return experimentId;
    }

    /**
     * Sets the post's experiment id
     * @param experimentId
     */
    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * Gets the text of the post
     * @return String of post text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the poster's id
     * @return String of poster id
     */
    public String getPosterId() {
        return posterId;
    }

    /**
     * Gets the id of the post itself
     * @return String of the post id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the post itself
     * @param id
     */
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
