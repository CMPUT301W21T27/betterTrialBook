package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bettertrialbook.Extras;

import java.util.Date;

/**
 * The BinomialTrial class extends the Trial class and represents pass-fail trials. A trial
 * can be either a single pass or a single fail.
 * The class implements Comparable so that it can be easily compared to itself.
 */
public class BinomialTrial extends Trial implements Comparable<BinomialTrial> {
    private boolean success;

    /**
     * Creates a binomial trial
     * @param success
     *  whether it was a success or failure
     * @param trialId
     *  the id of the trial
     * @param experimenterId
     *  the id of the experimenter that created the trial
     * @param geolocation
     *  the geolocation of the trial
     * @param timestamp
     *  the Date when it was created
     */
    public BinomialTrial(boolean success, String trialId, String experimenterId, Geolocation geolocation, Date timestamp) {
        this.success = success;
        setTimestamp(timestamp);
        setGeolocation(geolocation);
        setTrialID(trialId);
        setExperimenterID(experimenterId);
    }

    /**
     * get whether it was a success or failure
     * @return
     *  whether it was a success or failure
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * set whether it was a success or failure
     * @param success
     *  whether it was a success or failure
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * gets the type of trial/experiment associated with the trial
     * @return
     *  'Binomial'
     */
    @Override
    public String getTrialType() {
        return Extras.BINOMIAL_TYPE;
    }

    // Comparable implementation

    /**
     * This overrides the compareTo method from Comparable,
     * making it so that we can compare binomialTrial objects
     * @param binomialTrial
     *  The binomialTrial being compared to
     * @return
     *  Returns 0 if the binomialTrials are equal, non-zero otherwise
     */
    @Override
    public int compareTo(BinomialTrial binomialTrial) {
        return String.valueOf(this.success).compareTo(String.valueOf(binomialTrial.getSuccess())) + this.getTrialType().compareTo(binomialTrial.getTrialType())
                + this.getTrialID().compareTo(binomialTrial.getTrialID()) + this.getGeolocation().compareTo(binomialTrial.getGeolocation());
    }

    // Parcelable implementation, for the most part auto generated by Android Studio

    /**
     * The Constructor used for passing a Parcel
     * @param in
     *  the Parcel being used
     */
    protected BinomialTrial(Parcel in) {
        super(in);
        success = in.readBoolean();
    }

    /**
     * The Creator method for the Parcelable implementation of BinomialTrial
     */
    public static final Parcelable.Creator<BinomialTrial> CREATOR = new Parcelable.Creator<BinomialTrial>() {
        public BinomialTrial createFromParcel(Parcel in) {
            return new BinomialTrial(in);
        }

        public BinomialTrial[] newArray(int size) {
            return new BinomialTrial[size];
        }
    };

    /**
     * the describeContents() method implemented for Parcelable implementation
     * @return
     *  0
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Creates a parcel with the relevant information including whether the trial is a success or not
     * @param dest
     *  the Parcel being created
     * @param flags
     *  any relevant flags for Parcelable implementation
     */
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeBoolean(success);
    }
}
