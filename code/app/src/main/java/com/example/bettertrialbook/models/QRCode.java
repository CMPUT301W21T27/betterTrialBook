package com.example.bettertrialbook.models;

/**
 * Model class for QR Code
 */
public class QRCode {
    private String experimentId;
    private String trialId;
    private String id;

    // used by firebase to inflate
    public QRCode(){}

    public QRCode(String experimentId, String trialId, String id) {
        this.experimentId = experimentId;
        this.trialId = trialId;
        this.id = id;
    }

    /**
     * Gets the QR Code's associated experiment id
     * @return String of the experiment id
     */
    public String getExperimentId() {
        return experimentId;
    }

    /**
     * Sets the QR Code's associated experiment id
     * @param experimentId
     */
    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * Gets the QR Code's associated trial id
     * @return String of the trial id
     */
    public String getTrialId() {
        return trialId;
    }

    /**
     * Sets the QR Code's associated trial id
     * @param trialId
     */
    public void setTrialId(String trialId) {
        this.trialId = trialId;
    }

    /**
     * Gets the QR Code's id itself
     * @return String of the QR code id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the QR Code's id itself
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QRCode{" +
                "experimentId='" + experimentId + '\'' +
                ", trialId='" + trialId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
