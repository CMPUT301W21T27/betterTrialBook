package com.example.bettertrialbook.models;

public class BinomialTrial extends Trial {
    private int passCount;
    private int failCount;

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    @Override
    public String getTrialType() {
        return "Binomial";
    }
}
