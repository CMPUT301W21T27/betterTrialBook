/*
The NonNegTrial class extends the Trial class and represents non-negative integer count trials.
 */

package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bettertrialbook.Extras;
import com.google.type.DateTime;

import java.util.Date;

public class NonNegTrial extends Trial implements Comparable<NonNegTrial> {
    private int count;

    /**
     * creates a non negative integer trial
     * @param count
     *  the count being recorded
     * @param trialId
     *  the id of the trial
     * @param geolocation
     *  the geolocation of the trial
     * @param timestamp
     *  the Date when the trial was created
     */
    public NonNegTrial(int count, String trialId, String experimenterId, Geolocation geolocation, Date timestamp) {
        this.count = count;
        setTimestamp(timestamp);
        setTrialID(trialId);
        setExperimenterID(experimenterId);
        setGeolocation(geolocation);
    }

    /**
     * gets the recorded count for the trial
     * @return
     *  the count for the trial
     */
    public int getCount() {
        return count;
    }

    /**
     * sets the recorded count for the trial
     * @param count
     *  the count for the trial
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * gets the type of experiment/trial for the trial
     * @return
     *  'Non-Negative Integer'
     */
    @Override
    public String getTrialType() {
        return Extras.NONNEG_TYPE;
    }

    /**
     * This overrides the compareTo method from Comparable,
     * making it so that we can compare nonNegTrial objects
     * @param nonNegTrial
     *  The nonNegTrial being compared to
     * @return
     *  Returns 0 if the nonNegTrials are equal, non-zero otherwise
     */
    @Override
    public int compareTo(NonNegTrial nonNegTrial) {
        return String.valueOf(this.count).compareTo(String.valueOf(nonNegTrial.getCount()))
                + this.getTrialType().compareTo(nonNegTrial.getTrialType()) + this.getTrialID().compareTo(nonNegTrial.getTrialID())
                + this.getGeolocation().compareTo(nonNegTrial.getGeolocation());
    }

    public static final Parcelable.Creator<NonNegTrial> CREATOR = new Parcelable.Creator<NonNegTrial>() {
        public NonNegTrial createFromParcel(Parcel in) {
            return new NonNegTrial(in);
        }

        public NonNegTrial[] newArray(int size) {
            return new NonNegTrial[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
    }

    private NonNegTrial(Parcel in) {
        count = in.readInt();
    }
}
