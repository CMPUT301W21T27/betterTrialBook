/*
    Test Unit for LineGraph.java (Non negative Trial)
 */

package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.LineGraphInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class NonNegativeLineGraphInfoTest {
    private ArrayList<Trial> nonNegTrials;
    private ArrayList<Trial> nonNegEmptyTrials;
    private LineGraphInfo lineGraphInfo1;
    private LineGraphInfo lineGraphInfo2;

    private ArrayList<Trial> mockEmptyNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        return nonNeg;
    }

    private ArrayList<Trial> mockNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(2, "NonNegTestID2", "Person2", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID4", "Person4", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID6", "Person4", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(5, "NonNegTestID5", "Person5", new Geolocation(new Location("")), new Date()));
        return nonNeg;
    }

    @Before
    public void setUp() {
        nonNegTrials = mockNonNegTrials();
        nonNegEmptyTrials = mockEmptyNonNegTrials();

        lineGraphInfo1 = new LineGraphInfo(nonNegTrials, Extras.NONNEG_TYPE);
        lineGraphInfo2 = new LineGraphInfo(nonNegEmptyTrials, Extras.NONNEG_TYPE);
    }

    @Test
    public void testMeanOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.MeanOverTime();
        ArrayList<Double> result2 = lineGraphInfo2.MeanOverTime();

        assertEquals(1.0, result1.get(0));
        assertEquals(1.5, result1.get(1));
        assertEquals(2.0, result1.get(2));
        assertEquals(2.5, result1.get(3));
        assertEquals(2.8, result1.get(4));
        assertEquals(3.1667, result1.get(5));

        assertNull(result2);
    }

    @Test
    public void testMedianOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.MedianOverTime();
        ArrayList<Double> result2 = lineGraphInfo2.MedianOverTime();

        assertEquals(1.0, result1.get(0));
        assertEquals(1.5, result1.get(1));
        assertEquals(2.0, result1.get(2));
        assertEquals(2.5, result1.get(3));
        assertEquals(3.0, result1.get(4));
        assertEquals(3.5, result1.get(5));

        assertNull(result2);
    }

    @Test
    public void testStdDevOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.StdDevOverTime(lineGraphInfo1.MeanOverTime());
        ArrayList<Double> result2 = lineGraphInfo2.StdDevOverTime(lineGraphInfo2.MeanOverTime());

        assertEquals(0.0, result1.get(0));
        assertEquals(0.5, result1.get(1));
        assertEquals(0.8165, result1.get(2));
        assertEquals(1.118, result1.get(3));
        assertEquals(1.1662, result1.get(4));
        assertEquals(1.3437, result1.get(5));

        assertNull(result2);
    }
}
