// Testing Class: Copy from Chris Branch
package com.example.bettertrialbook;

public class ExperimentInfo {

    private String id;
    public String status;
    private String region;
    private int minTrials;
    private String trialType;
    private String description;
    private Boolean geoLocationRequired;

    public ExperimentInfo(String id, String description, String status, String trialType, boolean geoLocationRequired, int minTrials, String region) {
        this.id = id;
        this.status = status;
        this.region = region;
        this.minTrials = minTrials;
        this.trialType = trialType;
        this.description = description;
        this.geoLocationRequired = geoLocationRequired;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
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
