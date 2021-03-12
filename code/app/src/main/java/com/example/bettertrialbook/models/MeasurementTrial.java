package com.example.bettertrialbook.models;

public class MeasurementTrial extends Trial {
    private Double measurement;

    public Double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Double measurement) {
        this.measurement = measurement;
    }

    @Override
    public String getTrialType() {
        return "Measurement";
    }
}
