/*
    Test Unit for Statistic.java (Non Negative Trial)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.statistic.Statistic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class StatisticNonNegTest {
    private Statistic statistic = new Statistic();
    private ArrayList<Trial> nonNegOddTrials;
    private ArrayList<Trial> nonNegEvenTrials;
    private ArrayList<Trial> nonNegEmptyTrials;

    private ArrayList<Trial> mockEmptyNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        return nonNeg;
    }

    private ArrayList<Trial> mockEvenNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(2, "NonNegTestID2", "Person2", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID4", "Person4", new Geolocation(new Location("")), new Date()));
        return nonNeg;
    }

    private ArrayList<Trial> mockOddNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(2, "NonNegTestID2", "Person2", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID4", "Person4", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(5, "NonNegTestID5", "Person5", new Geolocation(new Location("")), new Date()));
        return nonNeg;
    }

    @Before
    public void setUp() {
        nonNegOddTrials = mockOddNonNegTrials();
        nonNegEvenTrials = mockEvenNonNegTrials();
        nonNegEmptyTrials = mockEmptyNonNegTrials();
    }

    @Test
    public void getTheDataTest() {
        ArrayList<Double> dataList = statistic.experimentData(nonNegOddTrials);

        for (int i = 0; i < nonNegOddTrials.size(); i++) {
            NonNegTrial trial = (NonNegTrial) nonNegOddTrials.get(i);
            assertEquals(trial.getCount(), dataList.get(i).intValue());
        }
    }

    @Test
    public void nonNegMeanTest() {
        double meanForOdd = statistic.Mean(nonNegOddTrials);
        double meanForEven = statistic.Mean(nonNegEvenTrials);
        double meanForEmpty = statistic.Mean(nonNegEmptyTrials);

        assertEquals(3.0, meanForOdd);
        assertEquals(2.5, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void nonNegMedianTest() {
        double medianForOdd = statistic.Median(nonNegOddTrials);
        double medianForEven = statistic.Median(nonNegEvenTrials);
        double medianForEmpty = statistic.Median(nonNegEmptyTrials);

        assertEquals(3.0, medianForOdd);
        assertEquals(2.5, medianForEven);
        assertEquals(0.0, medianForEmpty);
    }

    @Test
    public void nonNegStdDevTest() {
        double stdDevForOdd = statistic.StdDev(nonNegOddTrials, statistic.Mean(nonNegOddTrials));
        double stdDevForEven = statistic.StdDev(nonNegEvenTrials, statistic.Mean(nonNegEvenTrials));
        double stdDevForEmpty = statistic.StdDev(nonNegEmptyTrials, statistic.Mean(nonNegEmptyTrials));

        assertEquals(0.0, stdDevForEmpty);
        assertEquals(1.414, stdDevForOdd);
        assertEquals(1.118, stdDevForEven);
    }

    @Test
    public void nonNegQuartileTest() {
        double[] quartileForOdd = statistic.Quartiles(nonNegOddTrials);
        double[] quartileForEven = statistic.Quartiles(nonNegEvenTrials);
        double[] quartileForEmpty = statistic.Quartiles(nonNegEmptyTrials);

        // Special Case when there is only one item
        ArrayList<Trial> special = new ArrayList<>();
        special.add(new NonNegTrial(10, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        double[] quartileForSpecial = statistic.Quartiles(special);

        assertEquals(1.5, quartileForOdd[0]);
        assertEquals(4.5, quartileForOdd[1]);
        assertEquals(1.5, quartileForEven[0]);
        assertEquals(3.5, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
        assertEquals(10.0, quartileForSpecial[0]);
        assertEquals(10.0, quartileForSpecial[1]);
    }
}
