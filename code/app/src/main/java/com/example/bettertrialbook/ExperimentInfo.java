package com.example.bettertrialbook;

public class ExperimentInfo {
    private String description;
    // private User owner;  // class will be added later
    public String status;
    private int id;
    private String trialType;
    private Boolean geoLocationRequired;
    private int minTrials;
    private String region;

    public ExperimentInfo(String description, String status, String trialType, boolean geoLocationRequired, int minTrials, String region) {
        this.description = description;
        // this.owner = owner;
        this.status = status;
        this.id = id;
        this.trialType = trialType;
        this.geoLocationRequired = geoLocationRequired;
        this.minTrials = minTrials;
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    /*
    public User getOwner() {
        return owner;
    }

     */

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getTrialType() {
        return trialType;
    }

    public Boolean getGeoLocationRequired() {
        return geoLocationRequired;
    }

    public int getMinTrials() {
        return minTrials;
    }

    public String getRegion() {
        return region;
    }
}
