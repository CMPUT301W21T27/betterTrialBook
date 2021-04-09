/*
    Test Unit for Statistic.java (Measurement Trial)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Statistic;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.MeasurementTrial;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class StatisticMeasurementTrialTest {
    private ArrayList<Trial> mmOddTrials;
    private ArrayList<Trial> mmEvenTrials;
    private ArrayList<Trial> mmEmptyTrials;
    private Statistic statistic = new Statistic();

    private ArrayList<Trial> mockEmptyCountTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        return mm;
    }

    private ArrayList<Trial> mockEvenMeasurementTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(2.67, "MeasureTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(2.89, "MeasureTestID2", "Person2",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(1.656, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(3.4, "MeasureTestID4", "Person4",  new Geolocation(new Location("")), new Date()));
        return mm;
    }

    private ArrayList<Trial> mockOddMeasurementTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(8.56, "MeasureTestID1", "Person1",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(12.45, "MeasureTestID2", "Person2",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(10.34, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(7.99, "MeasureTestID4", "Person4",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(9.75, "MeasureTestID5", "Person5",  new Geolocation(new Location("")), new Date()));
        return mm;
    }

    @Before
    public void setUp() {
        mmOddTrials = mockOddMeasurementTrials();
        mmEvenTrials = mockEvenMeasurementTrials();
        mmEmptyTrials = mockEmptyCountTrials();
    }

    @Test
    public void testExperimentData() {
        ArrayList<Double> dataList = statistic.experimentData(mmOddTrials);

        // We should check if it is in sorted order.
        assertEquals(7.99, dataList.get(0));
        assertEquals(8.56, dataList.get(1));
        assertEquals(9.75, dataList.get(2));
        assertEquals(10.34, dataList.get(3));
        assertEquals(12.45, dataList.get(4));
    }

    @Test
    public void testMean() {
        double meanForOdd = statistic.Mean(mmOddTrials);
        double meanForEven = statistic.Mean(mmEvenTrials);
        double meanForEmpty = statistic.Mean(mmEmptyTrials);

        assertEquals(9.818, meanForOdd);
        assertEquals(2.654, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void testMedian() {
        double medianForOdd = statistic.Median(mmOddTrials);
        double medianForEven = statistic.Median(mmEvenTrials);
        double medianForEmpty = statistic.Median(mmEmptyTrials);

        assertEquals(9.75, medianForOdd);
        assertEquals(2.78, medianForEven);
        assertEquals(0.0, medianForEmpty);

    }

    @Test
    public void testStdDev() {
        double stdDevForOdd = statistic.StdDev(mmOddTrials, statistic.Mean(mmOddTrials));
        double stdDevForEven = statistic.StdDev(mmEvenTrials, statistic.Mean(mmEvenTrials));
        double stdDevForEmpty = statistic.StdDev(mmEmptyTrials, statistic.Mean(mmEmptyTrials));

        assertEquals(1.557, stdDevForOdd);
        assertEquals(0.634, stdDevForEven);
        assertEquals(0.0, stdDevForEmpty);
    }

    @Test
    public void testQuartiles() {
        double[] quartileForOdd = statistic.Quartiles(mmOddTrials);
        double[] quartileForEven = statistic.Quartiles(mmEvenTrials);
        double[] quartileForEmpty = statistic.Quartiles(mmEmptyTrials);

        // Special Case when there is only one item
        ArrayList<Trial> special = new ArrayList<>();
        special.add(new MeasurementTrial(1.5, "MeasureTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        double[] quartileForSpecial = statistic.Quartiles(special);

        assertEquals(8.275, quartileForOdd[0]);
        assertEquals(11.395, quartileForOdd[1]);
        assertEquals(2.163, quartileForEven[0]);
        assertEquals(3.145, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
        assertEquals(1.5, quartileForSpecial[0]);
        assertEquals(1.5, quartileForSpecial[1]);
    }
}
