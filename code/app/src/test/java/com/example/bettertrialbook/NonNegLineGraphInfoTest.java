package com.example.bettertrialbook;

import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.LineGraphInfo;
import com.example.bettertrialbook.statistic.Statistic;

import org.junit.Before;

import java.util.ArrayList;

public class NonNegLineGraphInfoTest {
    LineGraphInfo lineGraphInfo;
    private ArrayList<Trial> nonNegOddTrials;
    private ArrayList<Trial> nonNegEvenTrials;
    private ArrayList<Trial> nonNegEmptyTrials;

    private ArrayList<Trial> mockEmptyNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        return nonNeg;
    }

    private ArrayList<Trial> mockEvenNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1"));
        nonNeg.add(new NonNegTrial(2, "NonNegTestID2", "Person2"));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3"));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID4", "Person4"));
        return nonNeg;
    }

    private ArrayList<Trial> mockOddNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1"));
        nonNeg.add(new NonNegTrial(2, "NonNegTestID2", "Person2"));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3"));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID4", "Person4"));
        nonNeg.add(new NonNegTrial(5, "NonNegTestID5", "Person5"));
        return nonNeg;
    }

    @Before
    public void setUp() {
        lineGraphInfo = new LineGraphInfo();
        nonNegOddTrials = mockOddNonNegTrials();
        nonNegEvenTrials = mockEvenNonNegTrials();
        nonNegEmptyTrials = mockEmptyNonNegTrials();
    }
}
