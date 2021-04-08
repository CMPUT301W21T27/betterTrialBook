/*
    Test Unit HistogramInfo.java (Non Negative Trial)
 */

package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

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
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID2", "Person2", new Geolocation(new Location("")), new Date()));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3", new Geolocation(new Location("")), new Date()));
        return nonNeg;
    }

    private ArrayList<Trial> mockEvenNonNegTrials() {
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

        // Result for odd, smaller than 5
        assertEquals(1, (int) result1.get(0));
        assertEquals(2, (int) result1.get(1));
        // Result for even, larger or equal to 5
        assertEquals(0, (int) result2.get(0));
        assertEquals(1, (int) result2.get(1));
        assertEquals(1, (int) result2.get(2));
        assertEquals(1, (int) result2.get(3));
        assertEquals(3, (int) result2.get(4));
        // Result for Empty
        assertNull(result3);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> label1 = histogramInfo1.getLabels();
        ArrayList<String> label2 = histogramInfo2.getLabels();
        ArrayList<String> label3 = histogramInfo3.getLabels();

        // Result for odd, distinct item smaller than 5
        assertEquals("1.0", label1.get(0));
        assertEquals("3.0", label1.get(1));
        // Result for even, distinct item larger than or equal to 5
        assertEquals("0-0", label2.get(0));
        assertEquals("1-1", label2.get(1));
        assertEquals("2-2", label2.get(2));
        assertEquals("3-3", label2.get(3));
        assertEquals("4+", label2.get(4));
        // Result for empty
        assertNull(label3);
    }

}
