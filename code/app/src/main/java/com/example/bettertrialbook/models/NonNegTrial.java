package com.example.bettertrialbook.models;

public class NonNegTrial extends Trial {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String getTrialType() {
        return "Non-Negative-Integer";
    }
}
