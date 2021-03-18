package com.example.bettertrialbook.models;

public abstract class Trial {
    // private Geolocation geolocation;
    private String trialID;
    private String experimenterID;

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

    /**
     * sets the id of the trial
     * @param trialID
     *  the string id for the trial
     */
    public void setTrialID(String trialID) {
        this.trialID = trialID;
    }

    public String getExperimenterID() {
        return experimenterID;
    }

    /**
     * sets the trial's experimenter id
     * @param experimenterID
     *  the string id for the experimenter
     */
    public void setExperimenterID(String experimenterID) {
        this.experimenterID = experimenterID;
    }

    /**
     * gets the type of trial/experiment for the trial
     * note this is implemented in other classes extending this one
     * @return
     *  the type of trial/experiment for the trial
     */
    public abstract String getTrialType();
}
