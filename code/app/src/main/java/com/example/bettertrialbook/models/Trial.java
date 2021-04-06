/*
The abstract Trial class is a parent class for the four kinds of trials.
This holds the id of the trial and the experimenter, as well as contains
an abstract method getTrialType() to be implemented by each child class.
Currently geolocations and blacklisting for trials have yet to be implemented.
 */

package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public abstract class Trial implements Serializable{
    // private Geolocation geolocation;
    private String trialID;
    private String experimenterID;
    private Boolean blacklist = false;
    private Geolocation geolocation;

    /**
     * gets the geolocation of the Trial
     * @return
     *  the geolocation of the trial
     */
    public Geolocation getGeolocation() {
        return geolocation;
    }

    /**
     * sets the geolocation of the trial
     * @param geolocation
     *  the geolocation to be set
     */
    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * gets the id of the trial
     * @return
     *  the id of the trial
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

    /**
     * gets the experimenter who made the trial's id
     * @return
     *  the id of the experimenter who created the trial
     */
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

    public Boolean getBlacklist() {
        return blacklist;
    }

    /**
     * sets the trial's blacklist status
     * @param blacklist
     *  updates the current status of the trial's blacklist
     */
    public void setBlacklist(Boolean blacklist) {
        this.blacklist = blacklist;
    }
}
