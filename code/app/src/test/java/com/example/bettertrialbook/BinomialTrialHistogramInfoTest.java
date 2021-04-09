/*
    Test Unit for HistogramInfo.java (Binomial Trial)
 */

package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertEquals;

public class BinomialTrialHistogramInfoTest {
    private ArrayList<Trial> binomialTrialsOdd;
    private ArrayList<Trial> binomialTrialsEven;
    private ArrayList<Trial> binomialTrialsEmpty;
    private HistogramInfo histogramInfo1;
    private HistogramInfo histogramInfo2;
    private HistogramInfo histogramInfo3;

    private ArrayList<Trial> mockBinomialTrialsOdd() {
        ArrayList<Trial> mock = new ArrayList<>();

        mock.add(new BinomialTrial(true, "1", "Terence", new Geolocation(new Location("")), new Date()));
        mock.add(new BinomialTrial(true, "2", "Terence", new Geolocation(new Location("")), new Date()));
        mock.add(new BinomialTrial(false, "3", "Terence", new Geolocation(new Location("")), new Date()));

        return mock;
    }

    private ArrayList<Trial> mockBinomialTrialsEven() {
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
        binomialTrialsOdd = mockBinomialTrialsOdd();
        binomialTrialsEven = mockBinomialTrialsEven();
        binomialTrialsEmpty = mockBinomialTrialsEmpty();
        histogramInfo1 = new HistogramInfo(binomialTrialsOdd, Extras.BINOMIAL_TYPE);
        histogramInfo2 = new HistogramInfo(binomialTrialsEven, Extras.BINOMIAL_TYPE);
        histogramInfo3 = new HistogramInfo(binomialTrialsEmpty, Extras.BINOMIAL_TYPE);
    }

    @Test
    public void testCollectFrequency() {
        ArrayList<Integer> result1 = histogramInfo1.collectFrequency();
        ArrayList<Integer> result2 = histogramInfo2.collectFrequency();
        ArrayList<Integer> result3 = histogramInfo3.collectFrequency();


        // Result for distinct item < 5 in the data list
        assertEquals(1, (int) result1.get(0));
        assertEquals(2, (int) result1.get(1));
        // Result for distinct item >= 5 in the data List
        assertEquals(3, (int) result2.get(0));
        assertEquals(3, (int) result2.get(1));
        // Result for Empty data List
        assertNull(result3);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> label1 = histogramInfo1.getLabels();
        ArrayList<String> label2 = histogramInfo2.getLabels();
        ArrayList<String> label3 = histogramInfo3.getLabels();

        // Result for distinct items < 5 in the dataList
        assertEquals("Failure", label1.get(0));
        assertEquals("Success", label1.get(1));
        // Result for distinct items >= 5 in the dataList
        assertEquals("Failure", label2.get(0));
        assertEquals("Success", label2.get(1));
        // Result for empty data list
        assertNull(label3);
    }
}
