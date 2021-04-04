/*
The ExperimentInfo class contains important information about each experiment. This is used to
pass and retrieve experiment information from the database.
 */

package com.example.bettertrialbook.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class ExperimentInfo implements Comparable<ExperimentInfo>, Parcelable {
    private String description;
    private String ownerId;
    public String publishStatus;
    public String activeStatus;
    private String id;
    private String trialType;
    private Boolean geoLocationRequired;
    private int minTrials;
    private String region;

    /**
     * Creates an ExperimentInfo object with the following parameters
     * @param description
     *  A description of the experiment
     * @param ownerId
     *  The  id of the experiment's owner
     * @param publishStatus
     *  Either 'Published' or 'Unpublished'
     * @param activeStatus
     *  Either 'Active' or 'Ended'
     * @param id
     *  The unique id of the experiment
     * @param trialType
     *  The kind of trials required for the experiment (Binomial, Count-based, Measurement, Non-negative integer)
     * @param geoLocationRequired
     *  Whether or not geolocation is required for trials submitted to the experiment
     * @param minTrials
     *  The minimum number of trials required for the experiment
     * @param region
     *  The region the experiment is taking place in
     */
    public ExperimentInfo(String description, String ownerId, String publishStatus, String activeStatus,
                            String id, String trialType, boolean geoLocationRequired, int minTrials, String region) {
        this.description = description;
        this.ownerId = ownerId;
        this.publishStatus = publishStatus;
        this.activeStatus = activeStatus;
        this.id = id;
        this.trialType = trialType;
        this.geoLocationRequired = geoLocationRequired;
        this.minTrials = minTrials;
        this.region = region;
    }

    /**
     * Sets the id of the experiment
     * @param id
     *  the unique id of the experiment
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * gets the description of the experiment
     * @return
     *  the description
     */
    public String getDescription() {
        return description;
    }

    /*
    public User getOwner() {
        return owner;
    }

     */

    /**
     * gets the publish status of the experiment
     * @return
     *  the status ('Published', 'Unpublished')
     */
    public String getPublishStatus() {
        return publishStatus;
    }

    /**
     * gets the active status of the experiment
     * @return
     *  the status ('Active', 'Ended')
     */
    public String getActiveStatus() {
        return activeStatus;
    }

    /**
     * gets the ownerId of the experiment
     * @return
     *  the ownerId
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * gets the id of the experiment
     * @return
     *  unique id of the experiment
     */
    public String getId() {
        return id;
    }

    /**
     * gets the trial type of the experiment
     * @return
     *  the trial type (Binomial, Count-based, Measurement, Non-negative integer)
     */
    public String getTrialType() {
        return trialType;
    }

    /**
     * gets whether or not geolocation is required for the experiment
     * @return
     *  a boolean specifying required or not
     */
    public Boolean getGeoLocationRequired() {
        return geoLocationRequired;
    }

    /**
     * gets the min number of trials needed for the experiment
     * @return
     *  the min number of trials
     */
    public int getMinTrials() {
        return minTrials;
    }

    /**
     * gets the region of the experiment
     * @return
     *  the region
     */
    public String getRegion() {
        return region;
    }

    // I don't remember what this is for...did I add this?
    // Might not be necessary -Shasta
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }

        if (this.getClass() == obj.getClass()) {
            ExperimentInfo experimentInfo = (ExperimentInfo) obj;
            return this.id.equals(experimentInfo.getId()) && this.description.equals(experimentInfo.getDescription())
                    && this.publishStatus.equals(experimentInfo.getPublishStatus())
                    && this.activeStatus.equals(experimentInfo.getActiveStatus())
                    && this.trialType.equals(experimentInfo.getTrialType())
                    && this.geoLocationRequired.equals(experimentInfo.getGeoLocationRequired())
                    && this.minTrials == experimentInfo.getMinTrials()
                    && this.region.equals(experimentInfo.getRegion());

        } else {
            return false;
        }
    }

    /**
     * This overrides the compareTo method from Comparable,
     * making it so that we can compare experimentInfo objects
     * @param experimentInfo
     *  The experimentInfo being compared to
     * @return
     *  Returns 0 if the experimentInfos are equal, non-zero otherwise
     */
    @Override
    public int compareTo(ExperimentInfo experimentInfo) {
        return this.id.compareTo(experimentInfo.getId()) + this.description.compareTo(experimentInfo.getDescription())
                + this.publishStatus.compareTo(experimentInfo.getPublishStatus())
                + this.activeStatus.compareTo(experimentInfo.getActiveStatus())
                + this.trialType.compareTo(experimentInfo.getTrialType())
                + String.valueOf(this.geoLocationRequired).compareTo(String.valueOf(experimentInfo.getGeoLocationRequired()))
                + String.valueOf(this.minTrials).compareTo(String.valueOf(experimentInfo.getMinTrials()))
                + this.region.compareTo(experimentInfo.getRegion());
    }

    // parcelable implementation
    protected ExperimentInfo(Parcel in) {
        description = in.readString();
        ownerId = in.readString();
        publishStatus = in.readString();
        activeStatus = in.readString();
        id = in.readString();
        trialType = in.readString();
//        byte tmpGeoLocationRequired = in.readByte();
//        geoLocationRequired = tmpGeoLocationRequired == 0 ? null : tmpGeoLocationRequired == 1;
        geoLocationRequired = in.readBoolean();
        minTrials = in.readInt();
        region = in.readString();
    }

    public static final Creator<ExperimentInfo> CREATOR = new Creator<ExperimentInfo>() {
        @Override
        public ExperimentInfo createFromParcel(Parcel in) {
            return new ExperimentInfo(in);
        }

        @Override
        public ExperimentInfo[] newArray(int size) {
            return new ExperimentInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(ownerId);
        dest.writeString(publishStatus);
        dest.writeString(activeStatus);
        dest.writeString(id);
        dest.writeString(trialType);
        dest.writeBoolean(geoLocationRequired);
        dest.writeInt(minTrials);
        dest.writeString(region);

    }
}
