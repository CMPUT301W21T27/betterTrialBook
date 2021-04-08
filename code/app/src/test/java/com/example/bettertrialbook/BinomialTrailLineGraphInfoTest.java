/*
    Test Unit for LineGraphInfo.java (Binomial Trial)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.HistogramInfo;
import com.example.bettertrialbook.statistic.LineGraphInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class BinomialTrailLineGraphInfoTest {
    private ArrayList<Trial> binomialTrials;
    private ArrayList<Trial> binomialTrialsEmpty;
    private LineGraphInfo lineGraphInfo1;
    private LineGraphInfo lineGraphInfo2;

    private ArrayList<Trial> mockBinomialTrials() {
        ArrayList<Trial> mock = new ArrayList<>();

        mock.add(new BinomialTrial(true, "1", "Terence", new Geolocation(new Location("")), new Date()));
        mock.add(new BinomialTrial(true, "2", "Terence", new Geolocation(new Location("")), new Date()));
        mock.add(new BinomialTrial(true, "3", "Terence", new Geolocation(new Location("")), new Date()));
        mock.add(new BinomialTrial(false, "4", "Terence", new Geolocation(new Location("")), new Date()));
        mock.add(new BinomialTrial(false, "5", "Terence", new Geolocation(new Location("")), new Date()));
        mock.add(new BinomialTrial(false, "6", "Terence", new Geolocation(new Location("")), new Date()));

        return mock;
    }

    private ArrayList<Trial> mockBinomialTrialsEmpty() {
        return new ArrayList<Trial>();
    }

    @Before
    public void setUp() {
        binomialTrials = mockBinomialTrials();
        binomialTrialsEmpty = mockBinomialTrialsEmpty();
        lineGraphInfo1 = new LineGraphInfo(binomialTrials, Extras.BINOMIAL_TYPE);
        lineGraphInfo2 = new LineGraphInfo(binomialTrialsEmpty, Extras.BINOMIAL_TYPE);
    }

    @Test
    public void testMeanOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.MeanOverTime();
        ArrayList<Double> result2 = lineGraphInfo2.MeanOverTime();

        assertEquals(1.0, result1.get(0));
        assertEquals(1.0, result1.get(1));
        assertEquals(1.0, result1.get(2));
        assertEquals(0.75, result1.get(3));
        assertEquals(0.6, result1.get(4));
        assertEquals(0.5, result1.get(5));

        assertNull(result2);
    }

    @Test
    public void testMedianOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.MedianOverTime();
        ArrayList<Double> result2 = lineGraphInfo2.MedianOverTime();

        assertEquals(1.0, result1.get(0));
        assertEquals(1.0, result1.get(1));
        assertEquals(1.0, result1.get(2));
        assertEquals(1.0, result1.get(3));
        assertEquals(1.0, result1.get(4));
        assertEquals(0.5, result1.get(5));

        assertNull(result2);
    }

    @Test
    public void testStdDevOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.StdDevOverTime(lineGraphInfo1.MeanOverTime());
        ArrayList<Double> result2 = lineGraphInfo2.StdDevOverTime(lineGraphInfo2.MeanOverTime());

        assertEquals(0.0, result1.get(0));
        assertEquals(0.0, result1.get(1));
        assertEquals(0.0, result1.get(2));
        assertEquals(0.433, result1.get(3));
        assertEquals(0.49, result1.get(4));
        assertEquals(0.5, result1.get(5));

        assertNull(result2);
    }
}
