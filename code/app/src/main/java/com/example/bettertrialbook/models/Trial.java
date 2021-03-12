package com.example.bettertrialbook.models;

public abstract class Trial {
    // private Geolocation geolocation;
    private String trialID;

    /*
    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }
    */

    public String getTrialID() {
        return trialID;
    }

    public void setTrialID(String trialID) {
        this.trialID = trialID;
    }

    public abstract String getTrialType();
}
