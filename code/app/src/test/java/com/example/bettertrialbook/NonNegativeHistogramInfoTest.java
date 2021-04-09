/*
    Test Unit HistogramInfo.java (Non Negative Trial)
 */

package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertEquals;

public class NonNegativeHistogramInfoTest {
    private ArrayList<Trial> nonNegOddTrials;
    private ArrayList<Trial> nonNegEvenTrials;
    private ArrayList<Trial> nonNegEmptyTrials;
    private HistogramInfo histogramInfo1;
    private HistogramInfo histogramInfo2;
    private HistogramInfo histogramInfo3;

    private ArrayList<Trial> mockEmptyNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
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

    private ArrayList<Trial> mockEvenNonNegTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(7, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(9, "NonNegTestID2", "Person2", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID3", "Person3", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(5, "NonNegTestID4", "Person4", new Geolocation(new Location("")), new Date()));
        return nonNeg;
    }

    @Before
    public void setUp() {
        nonNegOddTrials = mockOddNonNegTrials();
        nonNegEvenTrials = mockEvenNonNegTrials();
        nonNegEmptyTrials = mockEmptyNonNegTrials();

        histogramInfo1 = new HistogramInfo(nonNegOddTrials, Extras.NONNEG_TYPE);
        histogramInfo2 = new HistogramInfo(nonNegEvenTrials, Extras.NONNEG_TYPE);
        histogramInfo3 = new HistogramInfo(nonNegEmptyTrials, Extras.NONNEG_TYPE);
    }

    @Test
    public void testCollectFrequency() {
        ArrayList<Integer> result1 = histogramInfo1.collectFrequency();
        ArrayList<Integer> result2 = histogramInfo2.collectFrequency();
        ArrayList<Integer> result3 = histogramInfo3.collectFrequency();

        // Result for distinct items >= 5 in the data list
        assertEquals(0, (int) result1.get(0));
        assertEquals(0, (int) result1.get(1));
        assertEquals(2, (int) result1.get(2));
        assertEquals(2, (int) result1.get(3));
        assertEquals(1, (int) result1.get(4));
        // Result for distinct items < 5 in the data list
        assertEquals(1, (int) result2.get(0));
        assertEquals(1, (int) result2.get(1));
        assertEquals(1, (int) result2.get(2));
        assertEquals(1, (int) result2.get(3));
        // Result for Empty in the data list.
        assertNull(result3);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> label1 = histogramInfo1.getLabels();
        ArrayList<String> label2 = histogramInfo2.getLabels();
        ArrayList<String> label3 = histogramInfo3.getLabels();

        // Result for distinct items >= 5 in the data list
        assertEquals("0-3", label1.get(0));
        assertEquals("4-7", label1.get(1));
        assertEquals("8-11", label1.get(2));
        assertEquals("12-15", label1.get(3));
        assertEquals("16+", label1.get(4));
        // Result for distinct items < 5 in the data list
        assertEquals("4.0", label2.get(0));
        assertEquals("5.0", label2.get(1));
        assertEquals("7.0", label2.get(2));
        assertEquals("9.0", label2.get(3));
        // Result for Empty in the data list.
        assertNull(label3);
    }

}
