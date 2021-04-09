/*
    Test Unit for Statistic.java (Non Negative Trial)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Statistic;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Geolocation;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class StatisticNonNegTest {
    private ArrayList<Trial> nonNegOddTrials;
    private ArrayList<Trial> nonNegEvenTrials;
    private ArrayList<Trial> nonNegEmptyTrials;
    private Statistic statistic = new Statistic();

    private ArrayList<Trial> mockEmptyNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        return nonNeg;
    }

    private ArrayList<Trial> mockEvenNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(7, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(9, "NonNegTestID2", "Person2", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID3", "Person3", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(5, "NonNegTestID4", "Person4", new Geolocation(new Location("")), new Date()));
        return nonNeg;
    }

    private ArrayList<Trial> mockOddNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(15, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(12, "NonNegTestID2", "Person2", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(25, "NonNegTestID3", "Person3", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(8, "NonNegTestID4", "Person4", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(11, "NonNegTestID5", "Person5", new Geolocation(new Location("")), new Date()));
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

        // Check if the dataList is sorted
        assertEquals(8.0, dataList.get(0));
        assertEquals(11.0, dataList.get(1));
        assertEquals(12.0, dataList.get(2));
        assertEquals(15.0, dataList.get(3));
        assertEquals(25.0, dataList.get(4));
    }

    @Test
    public void nonNegMeanTest() {
        double meanForOdd = statistic.Mean(nonNegOddTrials);
        double meanForEven = statistic.Mean(nonNegEvenTrials);
        double meanForEmpty = statistic.Mean(nonNegEmptyTrials);

        assertEquals(14.2, meanForOdd);
        assertEquals(6.25, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void nonNegMedianTest() {
        double medianForOdd = statistic.Median(nonNegOddTrials);
        double medianForEven = statistic.Median(nonNegEvenTrials);
        double medianForEmpty = statistic.Median(nonNegEmptyTrials);

        assertEquals(12.0, medianForOdd);
        assertEquals(6.0, medianForEven);
        assertEquals(0.0, medianForEmpty);
    }

    @Test
    public void nonNegStdDevTest() {
        double stdDevForOdd = statistic.StdDev(nonNegOddTrials, statistic.Mean(nonNegOddTrials));
        double stdDevForEven = statistic.StdDev(nonNegEvenTrials, statistic.Mean(nonNegEvenTrials));
        double stdDevForEmpty = statistic.StdDev(nonNegEmptyTrials, statistic.Mean(nonNegEmptyTrials));

        assertEquals(5.844656, stdDevForOdd);
        assertEquals(1.920286, stdDevForEven);
        assertEquals(0.0, stdDevForEmpty);
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

        assertEquals(9.5, quartileForOdd[0]);
        assertEquals(20.0, quartileForOdd[1]);
        assertEquals(4.5, quartileForEven[0]);
        assertEquals(8.0, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
        assertEquals(10.0, quartileForSpecial[0]);
        assertEquals(10.0, quartileForSpecial[1]);
    }
}
