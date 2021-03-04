package com.example.bettertrialbook;

public class ExperimentInfo {
    private String description;
    private User owner;  // class will be added later
    public String status;
    private int id;
    private String trialType;
    private Boolean geolocationRequired;
    private int minTrials;
    private String region;

    public ExperimentInfo(String description, User owner, String status, int id, String trialType, Boolean geolocationRequired, int minTrials, String region) {
        this.description = description;
        this.owner = owner;
        this.status = status;
        this.id = id;
        this.trialType = trialType;
        this.geolocationRequired = geolocationRequired;
        this.minTrials = minTrials;
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getTrialType() {
        return trialType;
    }

    public Boolean getGeolocationRequired() {
        return geolocationRequired;
    }

    public int getMinTrials() {
        return minTrials;
    }

    public String getRegion() {
        return region;
    }
}
