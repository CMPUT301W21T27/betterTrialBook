package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class MeasurementHistogramInfoTest {
    private ArrayList<Trial> mmOddTrials;
    private ArrayList<Trial> mmEvenTrials;
    private ArrayList<Trial> mmEmptyTrials;
    private HistogramInfo histogramInfo1;
    private HistogramInfo histogramInfo2;
    private HistogramInfo histogramInfo3;

    private ArrayList<Trial> mockEmptyMeasurementTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        return mm;
    }

    private ArrayList<Trial> mockEvenMeasurementTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(2.25, "MeasureTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(3.34, "MeasureTestID2", "Person2",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(2.20, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(2.32, "MeasureTestID4", "Person4",  new Geolocation(new Location("")), new Date()));
        return mm;
    }

    private ArrayList<Trial> mockOddMeasurementTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(3.29, "MeasureTestID1", "Person1",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(2.20, "MeasureTestID2", "Person2",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(1.10, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(3.30, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(5.50, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(4.40, "MeasureTestID4", "Person4",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(3.31, "MeasureTestID5", "Person5",  new Geolocation(new Location("")), new Date()));
        return mm;
    }

    @Before
    public void setUp() {
        mmOddTrials = mockOddMeasurementTrials();
        mmEvenTrials = mockEvenMeasurementTrials();
        mmEmptyTrials = mockEmptyMeasurementTrials();

        histogramInfo1 = new HistogramInfo(mmOddTrials, Extras.MEASUREMENT_TYPE);
        histogramInfo2 = new HistogramInfo(mmEvenTrials, Extras.MEASUREMENT_TYPE);
        histogramInfo3 = new HistogramInfo(mmEmptyTrials, Extras.MEASUREMENT_TYPE);
    }

    @Test
    public void testCollectFrequency() {
        ArrayList<Integer> result1 = histogramInfo1.collectFrequency();
        ArrayList<Integer> result2 = histogramInfo2.collectFrequency();
        ArrayList<Integer> result3 = histogramInfo3.collectFrequency();

        // Result for distinct item >= 5 in the data list
        assertEquals(0, (int) result1.get(0));
        assertEquals(1, (int) result1.get(1));
        assertEquals(1, (int) result1.get(2));
        assertEquals(3, (int) result1.get(3));
        assertEquals(2, (int) result1.get(4));
        // Result for distinct item < 5 in the data list
        assertEquals(1, (int) result2.get(0));
        assertEquals(1, (int) result2.get(1));
        assertEquals(1, (int) result2.get(2));
        assertEquals(1, (int) result2.get(3));
        // Result Empty
        assertNull(result3);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> label1 = histogramInfo1.getLabels();
        ArrayList<String> label2 = histogramInfo2.getLabels();
        ArrayList<String> label3 = histogramInfo3.getLabels();

        // Result for distinct item >= 5 in the data list
        assertEquals("0-1", label1.get(0));
        assertEquals("1-2", label1.get(1));
        assertEquals("2-3", label1.get(2));
        assertEquals("3-4", label1.get(3));
        assertEquals("4+", label1.get(4));
        // Result for distinct item < 5 in the data list
        assertEquals("2.2", label2.get(0));
        assertEquals("2.25", label2.get(1));
        assertEquals("2.32", label2.get(2));
        assertEquals("3.34", label2.get(3));
        // Result Empty
        assertNull(label3);
    }
}
