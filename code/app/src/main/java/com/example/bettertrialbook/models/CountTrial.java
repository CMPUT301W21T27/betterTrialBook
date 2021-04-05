/*
The CountTrial class extends the Trial class and represents trials with counts.
 */

package com.example.bettertrialbook.models;

import com.example.bettertrialbook.Extras;

import java.util.ArrayList;

public class CountTrial extends Trial implements Comparable<CountTrial> {
    private int count;

    /**
     * creates a count trial
     * @param count
     *  the recorded number of counts for the trial
     * @param trialId
     *  the id of the trial
     * @param geolocation
     *  the geolocation of the trial
     */
    public CountTrial(int count, String trialId, String experimenterId, Geolocation geolocation) {
        this.count = count;
        setTrialID(trialId);
        setExperimenterID(experimenterId);
        setGeolocation(geolocation);
    }

    /**
     * gets the count recorded for the trial
     * @return
     *  the count for the trial
     */
    public int getCount() {
        return count;
    }

    /**
     * sets the count recorded for the trial
     * @param count
     *  the count for the trial
     */
    public void setCount(int count) {
        this.count = count;
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
        return String.valueOf(this.count).compareTo(String.valueOf(countTrial.getCount()))
                + this.getTrialType().compareTo(countTrial.getTrialType()) + this.getTrialID().compareTo(countTrial.getTrialID()) + this.getGeolocation().compareTo(countTrial.getGeolocation());
    }
}
