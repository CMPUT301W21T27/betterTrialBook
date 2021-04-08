/*
    Test Unit for LineGraphInfo.java (Measurement Trial)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.LineGraphInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class MeasurementTrialLineGraphInfoTest {
    private ArrayList<Trial> mmTrials;
    private ArrayList<Trial> mmEmptyTrials;
    private LineGraphInfo lineGraphInfo1;
    private LineGraphInfo lineGraphInfo2;

    private ArrayList<Trial> mockEmptyMeasurementTrial() {
        ArrayList<Trial> mm = new ArrayList<>();
        return mm;
    }

    private ArrayList<Trial> mockMeasurementTrial() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(1.1, "MeasureTestID1", "Person1",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(2.2, "MeasureTestID2", "Person2",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(3.3, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(4.4, "MeasureTestID4", "Person4",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(5.5, "MeasureTestID5", "Person5",  new Geolocation(new Location("")), new Date()));
        return mm;
    }

    @Before
    public void setUp() {
        mmTrials = mockMeasurementTrial();
        mmEmptyTrials = mockEmptyMeasurementTrial();

        lineGraphInfo1 = new LineGraphInfo(mmTrials, Extras.MEASUREMENT_TYPE);
        lineGraphInfo2 = new LineGraphInfo(mmEmptyTrials, Extras.MEASUREMENT_TYPE);
    }

    @Test
    public void testMeanOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.MeanOverTime();
        ArrayList<Double> result2 = lineGraphInfo2.MeanOverTime();

        assertEquals(1.1, result1.get(0));
        assertEquals(1.65, result1.get(1));
        assertEquals(2.2, result1.get(2));
        assertEquals(2.75, result1.get(3));
        assertEquals(3.3, result1.get(4));

        assertNull(result2);
    }

    @Test
    public void testMedianOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.MedianOverTime();
        ArrayList<Double> result2 = lineGraphInfo2.MedianOverTime();

        assertEquals(1.1, result1.get(0));
        assertEquals(1.65, result1.get(1));
        assertEquals(2.2, result1.get(2));
        assertEquals(2.75, result1.get(3));
        assertEquals(3.3, result1.get(4));

        assertNull(result2);
    }

    @Test
    public void testStdDevOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.StdDevOverTime(lineGraphInfo1.MeanOverTime());
        ArrayList<Double> result2 = lineGraphInfo2.StdDevOverTime(lineGraphInfo2.MeanOverTime());

        assertEquals(0.0, result1.get(0));
        assertEquals(0.55, result1.get(1));
        assertEquals(0.898, result1.get(2));
        assertEquals(1.23, result1.get(3));
        assertEquals(1.556, result1.get(4));

        assertNull(result2);
    }
}
