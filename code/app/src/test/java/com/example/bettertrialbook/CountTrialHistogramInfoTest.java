/*
    Test Unit for HistogramInfo.java (Count-Based Trial)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class CountTrialHistogramInfoTest {
    private ArrayList<Trial> countTrialOdd;
    private ArrayList<Trial> countTrialEven;
    private ArrayList<Trial> countTrialEmpty;
    private HistogramInfo histogramInfo1;
    private HistogramInfo histogramInfo2;
    private HistogramInfo histogramInfo3;

    private ArrayList<Trial> mockCountTrialEmpty() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        return countTrial;
    }

    private ArrayList<Trial> mockCountTrialOdd() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        countTrial.add(new CountTrial("1", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));

        return countTrial;
    }

    private ArrayList<Trial> mockCountTrialEven() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        countTrial.add(new CountTrial("1", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("3", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("4", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("5", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("6", "Terence", new Geolocation(new Location("")), new Date()));

        return countTrial;
    }

    @Before
    public void setUp() {
        countTrialOdd = mockCountTrialOdd();
        countTrialEven = mockCountTrialEven();
        countTrialEmpty = mockCountTrialEmpty();

        histogramInfo1 = new HistogramInfo(countTrialOdd, Extras.COUNT_TYPE);
        histogramInfo2 = new HistogramInfo(countTrialEven, Extras.COUNT_TYPE);
        histogramInfo3 = new HistogramInfo(countTrialEmpty, Extras.COUNT_TYPE);
    }

    @Test
    public void testCollectFrequency() {
        ArrayList<Integer> result1 = histogramInfo1.collectFrequency();
        ArrayList<Integer> result2 = histogramInfo2.collectFrequency();
        ArrayList<Integer> result3 = histogramInfo3.collectFrequency();

        // Result for odd, distinct item smaller than 5
        assertEquals(3, (int) result1.get(0));
        // Result for even, distinct item larger than or equal to 5
        assertEquals(6, (int) result2.get(0));
        // Result for empty
        assertNull(result3);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> label1 = histogramInfo1.getLabels();
        ArrayList<String> label2 = histogramInfo2.getLabels();
        ArrayList<String> label3 = histogramInfo3.getLabels();

        // Result for odd, distinct item smaller than 5
        assertEquals("Count", label1.get(0));
        // Result for even, distinct item larger than or equal to 5
        assertEquals("Count", label2.get(0));
        // Result for empty
        assertNull(label3);
    }
}
