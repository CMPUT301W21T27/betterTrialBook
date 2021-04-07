/*
The MeasurementTrial class extends the Trial class and represents measurement trials with
values of the Double type.
 */

package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bettertrialbook.Extras;

public class MeasurementTrial extends Trial implements Comparable<MeasurementTrial> {
    private Double measurement;

    /**
     * creates a measurement trial
     * @param measurement
     *  the double measurement being recorded
     * @param trialId
     *  the id of the trial
     * @param geolocation
     *  the geolocation of the trial
     */
    public MeasurementTrial(Double measurement, String trialId, String experimenterId, Geolocation geolocation) {
        this.measurement = measurement;
        setTrialID(trialId);
        setExperimenterID(experimenterId);
        setGeolocation(geolocation);
    }

    /**
     * gets the recorded measurement value for the trial
     * @return
     *  the measurement for the trial
     */
    public Double getMeasurement() {
        return measurement;
    }

    /**
     * sets the recorded measurement value for the trial
     * @param measurement
     *  the measurement for the trial
     */
    public void setMeasurement(Double measurement) {
        this.measurement = measurement;
    }

    /**
     * gets the trial/experiment type for the trial
     * @return
     *  'Measurement'
     */
    @Override
    public String getTrialType() {
        return Extras.MEASUREMENT_TYPE;
    }

    /**
     * This overrides the compareTo method from Comparable,
     * making it so that we can compare measurementTrial objects
     * @param measurementTrial
     *  The measurementTrial being compared to
     * @return
     *  Returns 0 if the measurementTrials are equal, non-zero otherwise
     */
    @Override
    public int compareTo(MeasurementTrial measurementTrial) {
        return String.valueOf(this.measurement).compareTo(String.valueOf(measurementTrial.getMeasurement()))
                + this.getTrialType().compareTo(measurementTrial.getTrialType()) + this.getTrialID().compareTo(measurementTrial.getTrialID())
                + this.getGeolocation().compareTo(measurementTrial.getGeolocation());
    }

    public static final Parcelable.Creator<MeasurementTrial> CREATOR = new Parcelable.Creator<MeasurementTrial>() {
        public MeasurementTrial createFromParcel(Parcel in) {
            return new MeasurementTrial(in);
        }

        public MeasurementTrial[] newArray(int size) {
            return new MeasurementTrial[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(measurement);
    }

    private MeasurementTrial(Parcel in) {
        measurement = in.readDouble();
    }
}
