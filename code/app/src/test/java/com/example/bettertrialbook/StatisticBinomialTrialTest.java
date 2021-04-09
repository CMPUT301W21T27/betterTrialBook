/*
    Test Unit For Statistic.java (BinomialTrial Version)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Statistic;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.BinomialTrial;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class StatisticBinomialTrialTest {
    Statistic statistic = new Statistic();
    private ArrayList<Trial> binomialTrialsOdd;
    private ArrayList<Trial> binomialTrialsEven;
    private ArrayList<Trial> binomialTrialsEmpty;

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
    }

    @Test
    public void testExperimentData() {
        ArrayList<Double> sorted = statistic.experimentData(mockBinomialTrialsOdd());

        // Check if the dataList is sorted
        assertEquals(0.0, sorted.get(0));
        assertEquals(1.0, sorted.get(1));
        assertEquals(1.0, sorted.get(1));
    }

    @Test
    public void testMean() {
        double mean1 = statistic.Mean(binomialTrialsEven);
        double mean2 = statistic.Mean(binomialTrialsEmpty);
        double mean3 = statistic.Mean(binomialTrialsOdd);

        assertEquals(0.5, mean1);
        assertEquals(0.0, mean2);
        assertEquals(0.667, mean3);
    }

    @Test
    public void testMedian() {
        double median1 = statistic.Median(binomialTrialsEven);
        double median2 = statistic.Median(binomialTrialsEmpty);
        double median3 = statistic.Median(binomialTrialsOdd);

        assertEquals(0.5, median1);
        assertEquals(0.0, median2);
        assertEquals(1.0, median3);
    }

    @Test
    public void testStdDev() {
        double stdDev1 = statistic.StdDev(binomialTrialsEven, statistic.Mean(binomialTrialsEven));
        double stdDev2 = statistic.StdDev(binomialTrialsEmpty, statistic.Mean(binomialTrialsEmpty));
        double stdDev3 = statistic.StdDev(binomialTrialsOdd, statistic.Mean(binomialTrialsOdd));

        assertEquals(0.5, stdDev1);
        assertEquals(0.0, stdDev2);
        assertEquals(0.471, stdDev3);
    }

    @Test
    public void testQuartiles() {
        double[] quartile1 = statistic.Quartiles(binomialTrialsEven);
        double[] quartile2 = statistic.Quartiles(binomialTrialsEmpty);
        double[] quartile3 = statistic.Quartiles(binomialTrialsOdd);

        // Special Case when there is only one item
        ArrayList<Trial> special = new ArrayList<>();
        special.add(new BinomialTrial(true, "99", "Terence", new Geolocation(new Location("")), new Date()));
        double[] quartile4 = statistic.Quartiles(special);

        assertEquals(0.0, quartile1[0]);
        assertEquals(1.0, quartile1[1]);
        assertEquals(0.0, quartile2[0]);
        assertEquals(0.0, quartile2[1]);
        assertEquals(0.0, quartile3[0]);
        assertEquals(1.0, quartile3[1]);
        assertEquals(1.0, quartile4[0]);
        assertEquals(1.0, quartile4[1]);
    }
}
