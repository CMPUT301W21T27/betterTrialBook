/*
    Test Unit for Statistic.java (CountTrial Version)

    Remarks:
    1)  experimentData will not be tested in CountTrial Version
        Since all the value in the dataSet is 1.0, which indicates as observed.
    2)  The results (Mean, Median, Standard Deviation, Quartiles) are meaningless in such type.
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Statistic;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class StatisticCountTrialTest {
    Statistic statistic = new Statistic();
    private ArrayList<Trial> countTrialOdd;
    private ArrayList<Trial> countTrialEven;
    private ArrayList<Trial> countTrialEmpty;

    private ArrayList<Trial> mockCountTrialEmpty() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        return countTrial;
    }

    private ArrayList<Trial> mockCountTrialEven() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        countTrial.add(new CountTrial("1", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("3", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("4", "Terence", new Geolocation(new Location("")), new Date()));

        return countTrial;
    }

    private ArrayList<Trial> mockCountTrialOdd() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        countTrial.add(new CountTrial("1", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("3", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("4", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("5", "Terence", new Geolocation(new Location("")), new Date()));

        return countTrial;
    }

    @Before
    public void setUp() {
        countTrialOdd = mockCountTrialOdd();
        countTrialEven = mockCountTrialEven();
        countTrialEmpty = mockCountTrialEmpty();
    }

    @Test
    public void testMean() {
        double mean1 = statistic.Mean(countTrialOdd);
        double mean2 = statistic.Mean(countTrialEven);
        double mean3 = statistic.Mean(countTrialEmpty);

        assertEquals(1.0, mean1);
        assertEquals(1.0, mean2);
        assertEquals(0.0, mean3);
    }

    @Test
    public void testMedian() {
        double median1 = statistic.Median(countTrialOdd);
        double median2 = statistic.Median(countTrialEven);
        double median3 = statistic.Median(countTrialEmpty);

        assertEquals(1.0, median1);
        assertEquals(1.0, median2);
        assertEquals(0.0, median3);
    }

    @Test
    public void testStdDev() {
        double stdDev1 = statistic.StdDev(countTrialOdd, statistic.Mean(countTrialOdd));
        double stdDev2 = statistic.StdDev(countTrialEven, statistic.Mean(countTrialEven));
        double stdDev3 = statistic.StdDev(countTrialEmpty, statistic.Mean(countTrialEmpty));

        assertEquals(0.0, stdDev1);
        assertEquals(0.0, stdDev2);
        assertEquals(0.0, stdDev3);
    }

    @Test
    public void testQuartiles() {
        double[] quartile1 = statistic.Quartiles(countTrialOdd);
        double[] quartile2 = statistic.Quartiles(countTrialEven);
        double[] quartile3 = statistic.Quartiles(countTrialEmpty);

        // Special Case when there is only one item
        ArrayList<Trial> special = new ArrayList<>();
        special.add(new CountTrial("99", "Terence", new Geolocation(new Location("")), new Date()));
        double[] quartile4 = statistic.Quartiles(special);

        assertEquals(1.0, quartile1[0]);
        assertEquals(1.0, quartile1[1]);
        assertEquals(1.0, quartile2[0]);
        assertEquals(1.0, quartile2[1]);
        assertEquals(0.0, quartile3[0]);
        assertEquals(0.0, quartile3[1]);
        assertEquals(1.0, quartile1[0]);
        assertEquals(1.0, quartile1[0]);
    }
}
