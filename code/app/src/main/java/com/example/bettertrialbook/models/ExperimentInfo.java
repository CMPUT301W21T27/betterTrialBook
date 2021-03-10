package com.example.bettertrialbook.models;

public class ExperimentInfo implements Comparable<ExperimentInfo> {
    private String description;
    // private User owner;  // class will be added later
    public String status;
    private String id;
    private String trialType;
    private Boolean geoLocationRequired;
    private int minTrials;
    private String region;

    /**
     * Creates an ExperimentInfo object with the following parameters
     * @param id
     *  The unique id of the experiment
     * @param description
     *  A description of the experiment
     * @param status
     *  Either 'Active', 'Unpublished', or 'Ended'
     * @param trialType
     *  The kind of trials required for the experiment (Binomial, Count-based, Measurement, Non-negative integer)
     * @param geoLocationRequired
     *  Whether or not geolocation is required for trials submitted to the experiment
     * @param minTrials
     *  The minimum number of trials required for the experiment
     * @param region
     *  The region the experiment is taking place in
     */
    public ExperimentInfo(String id, String description, String status, String trialType, boolean geoLocationRequired, int minTrials, String region) {
        this.description = description;
        // this.owner = owner;
        this.status = status;
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
     * gets the status of the experiment
     * @return
     *  the status ('Active', 'Unpublished', 'Ended')
     */
    public String getStatus() {
        return status;
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

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }

        if (this.getClass() == obj.getClass()){
            ExperimentInfo experimentInfo = (ExperimentInfo) obj;
            return this.id.equals(experimentInfo.getId()) && this.description.equals(experimentInfo.getDescription())
                    && this.status.equals(experimentInfo.getStatus()) && this.trialType.equals(experimentInfo.getTrialType())
                    && this.geoLocationRequired.equals(experimentInfo.getGeoLocationRequired()) && this.minTrials == experimentInfo.getMinTrials()
                    && this.region.equals(experimentInfo.getRegion());

        }else{
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
                + this.status.compareTo(experimentInfo.getStatus()) + this.trialType.compareTo(experimentInfo.getTrialType())
                + String.valueOf(this.geoLocationRequired).compareTo(String.valueOf(experimentInfo.getGeoLocationRequired())) + String.valueOf(this.minTrials).compareTo(String.valueOf(experimentInfo.getMinTrials()))
                + this.region.compareTo(experimentInfo.getRegion());
    }
}
