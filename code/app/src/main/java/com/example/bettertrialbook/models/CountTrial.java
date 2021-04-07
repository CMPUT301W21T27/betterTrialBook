/*
The CountTrial class extends the Trial class and represents trials with counts.
 */

package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bettertrialbook.Extras;

import java.util.ArrayList;
import java.util.Date;

public class CountTrial extends Trial implements Comparable<CountTrial> {

    /**
     * creates a count trial
     * @param trialId
     *  the id of the trial
     * @param geolocation
     *  the geolocation of the trial
     * @param timestamp
     *  the Date it was created
     */
    public CountTrial(String trialId, String experimenterId, Geolocation geolocation, Date timestamp) {
        setTimestamp(timestamp);
        setTrialID(trialId);
        setExperimenterID(experimenterId);
        setGeolocation(geolocation);
    }
    
    /**
     * gets the trial/experiment type of the trial
     * @return
     *  'Count-Based'
     */
    @Override
    public String getTrialType() {
        return Extras.COUNT_TYPE;
    }

    /**
     * This overrides the compareTo method from Comparable,
     * making it so that we can compare countTrial objects
     * @param countTrial
     *  The countTrial being compared to
     * @return
     *  Returns 0 if the countTrials are equal, non-zero otherwise
     */
    @Override
    public int compareTo(CountTrial countTrial) {
        return this.getTrialType().compareTo(countTrial.getTrialType()) + this.getTrialID().compareTo(countTrial.getTrialID()) + this.getGeolocation().compareTo(countTrial.getGeolocation());
    }

    public static final Parcelable.Creator<CountTrial> CREATOR = new Parcelable.Creator<CountTrial>() {
        public CountTrial createFromParcel(Parcel in) {
            return new CountTrial(in);
        }

        public CountTrial[] newArray(int size) {
            return new CountTrial[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {}

    private CountTrial(Parcel in) {}
}
