/*
The BinomialTrial class extends the Trial class and represents pass-fail trials.
 */

package com.example.bettertrialbook.models;

import com.example.bettertrialbook.Extras;

public class BinomialTrial extends Trial implements Comparable<BinomialTrial> {
    private int passCount;
    private int failCount;

    /**
     * Creates a binomial trial
     * @param passCount
     *  the number of recorded passes
     * @param failCount
     *  the number of recorded failures
     * @param trialId
     *  the id of the trial
     */
    public BinomialTrial(int passCount, int failCount, String trialId, String experimenterId) {
        this.passCount = passCount;
        this.failCount = failCount;
        setTrialID(trialId);
        setExperimenterID(experimenterId);
    }

    /**
     * get the number of passes recorded for the trial
     * @return
     *  the number of passes for the trial
     */
    public int getPassCount() {
        return passCount;
    }

    /**
     * set the number of passes recorded for the trial
     * @param passCount
     *  the number of passes for the trial
     */
    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    /**
     * get the number of failures recorded for the trial
     * @return
     *  the number of failures for the trial
     */
    public int getFailCount() {
        return failCount;
    }

    /**
     * set the number of failures recorded for the trial
     * @param failCount
     *  the number of failures for the trial
     */
    public void setFailCount(int failCount) {
        this.failCount = failCount;
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
        return String.valueOf(this.passCount).compareTo(String.valueOf(binomialTrial.getPassCount())) + String.valueOf(this.failCount).compareTo(String.valueOf(binomialTrial.getFailCount()))
                + this.getTrialType().compareTo(binomialTrial.getTrialType()) + this.getTrialID().compareTo(binomialTrial.getTrialID());
    }
}
