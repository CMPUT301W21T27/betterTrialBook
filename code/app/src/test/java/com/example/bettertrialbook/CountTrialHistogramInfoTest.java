package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

/**
 * Test Unit for HistogramInfo.java (Count-Based Trial)
 * Remarks:CountTrial always require only 1 bin (Count).
 */
public class CountTrialHistogramInfoTest {
    private ArrayList<Trial> countTrial;
    private ArrayList<Trial> countTrialEmpty;
    private HistogramInfo histogramInfo1;
    private HistogramInfo histogramInfo2;

    private ArrayList<Trial> mockCountTrialEmpty() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        return countTrial;
    }

    private ArrayList<Trial> mockCountTrial() {
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
        countTrial = mockCountTrial();
        countTrialEmpty = mockCountTrialEmpty();

        histogramInfo1 = new HistogramInfo(countTrial, Extras.COUNT_TYPE);
        histogramInfo2 = new HistogramInfo(countTrialEmpty, Extras.COUNT_TYPE);
    }

    @Test
    public void testCollectFrequency() {
        ArrayList<Integer> result1 = histogramInfo1.collectFrequency();
        ArrayList<Integer> result2 = histogramInfo2.collectFrequency();

        assertEquals(6, (int) result1.get(0));
        assertNull(result2);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> label1 = histogramInfo1.getLabels();
        ArrayList<String> label2 = histogramInfo2.getLabels();

        assertEquals("Count", label1.get(0));

        assertNull(label2);
    }
}
