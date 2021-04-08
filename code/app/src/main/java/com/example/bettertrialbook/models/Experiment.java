package com.example.bettertrialbook.models;

/**
 * The Experiment class contains an ExperimentInfo object for an Experiment
 */
public class Experiment {
    private ExperimentInfo info;

    /**
     * Constructor for an Experiment
     * @param info
     *  The ExperimentInfo included in the experiment
     */
    public Experiment(ExperimentInfo info) {
        this.info = info;
    }

    /**
     * gets the associated ExperimentInfo
     * @return
     *  the experiment's information
     */
    public ExperimentInfo getInfo() {
        return info;
    }
}
