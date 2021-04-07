/*
    Test Unit for Statistic.java (Measurement Trial)
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.Statistic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class StatisticMeasurementTrialTest {
    private Statistic statistic = new Statistic();
    private ArrayList<Trial> mmOddTrials;
    private ArrayList<Trial> mmEvenTrials;
    private ArrayList<Trial> mmEmptyTrials;

    private ArrayList<Trial> mockEmptyCountTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        return mm;
    }

    private ArrayList<Trial> mockEvenCountTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(1.5, "MeasureTestID1", "Person1", new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(3.5, "MeasureTestID2", "Person2",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(4.5, "MeasureTestID3", "Person3",  new Geolocation(new Location("")), new Date()));
        mm.add(new MeasurementTrial(6.5, "MeasureTestID4", "Person4",  new Geolocation(new Location("")), new Date()));
        return mm;
    }

    private ArrayList<Trial> mockOddCountTrials() {
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
        mmOddTrials = mockOddCountTrials();
        mmEvenTrials = mockEvenCountTrials();
        mmEmptyTrials = mockEmptyCountTrials();
    }

    @Test
    public void testExperimentData() {
        ArrayList<Double> dataList = statistic.experimentData(mmOddTrials);

        for (int i = 0; i < mmOddTrials.size(); i++) {
            MeasurementTrial trial = (MeasurementTrial) mmOddTrials.get(i);
            assertEquals(trial.getMeasurement(), dataList.get(i));
        }
    }

    @Test
    public void testMean() {
        double meanForOdd = statistic.Mean(mmOddTrials);
        double meanForEven = statistic.Mean(mmEvenTrials);
        double meanForEmpty = statistic.Mean(mmEmptyTrials);

        assertEquals(3.3, meanForOdd);
        assertEquals(4.0, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void testMedian() {
        double medianForOdd = statistic.Median(mmOddTrials);
        double medianForEven = statistic.Median(mmEvenTrials);
        double medianForEmpty = statistic.Median(mmEmptyTrials);

        assertEquals(3.3, medianForOdd);
        assertEquals(4.0, medianForEven);
        assertEquals(0.0, medianForEmpty);

    }

    @Test
    public void testStdDev() {
        double stdDevForOdd = statistic.StdDev(mmOddTrials, statistic.Mean(mmOddTrials));
        double stdDevForEven = statistic.StdDev(mmEvenTrials, statistic.Mean(mmEvenTrials));
        double stdDevForEmpty = statistic.StdDev(mmEmptyTrials, statistic.Mean(mmEmptyTrials));

        assertEquals(0.0, stdDevForEmpty);
        assertEquals(1.556, stdDevForOdd);
        assertEquals(1.803, stdDevForEven);
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

        assertEquals(1.65, quartileForOdd[0]);
        assertEquals(4.95, quartileForOdd[1]);
        assertEquals(2.5, quartileForEven[0]);
        assertEquals(5.5, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
        assertEquals(1.5, quartileForSpecial[0]);
        assertEquals(1.5, quartileForSpecial[1]);
    }
}
