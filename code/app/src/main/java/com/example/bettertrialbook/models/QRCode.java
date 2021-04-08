/*
Currently not implemented
 */

package com.example.bettertrialbook.models;


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

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getTrialId() {
        return trialId;
    }

    public void setTrialId(String trialId) {
        this.trialId = trialId;
    }

    public String getId() {
        return id;
    }

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
