/* All the expected value calculation are done by hand
 * Round Up to 3 decimal places
 */
package com.example.bettertrialbook;

import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.Statistic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class MeasurementTrialStatisticTest {
    private Statistic statistic;
    private ArrayList<Trial> mmOddTrials;
    private ArrayList<Trial> mmEvenTrials;
    private ArrayList<Trial> mmEmptyTrials;

    private ArrayList<Trial> mockEmptyCountTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        return mm;
    }

    private ArrayList<Trial> mockEvenCountTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(1.5, "MeasureTestID1", "Person1"));
        mm.add(new MeasurementTrial(3.5, "MeasureTestID2", "Person2"));
        mm.add(new MeasurementTrial(4.5, "MeasureTestID3", "Person3"));
        mm.add(new MeasurementTrial(6.5, "MeasureTestID4", "Person4"));
        return mm;
    }

    private ArrayList<Trial> mockOddCountTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        mm.add(new MeasurementTrial(1.1, "MeasureTestID1", "Person1"));
        mm.add(new MeasurementTrial(2.2, "MeasureTestID2", "Person2"));
        mm.add(new MeasurementTrial(3.3, "MeasureTestID3", "Person3"));
        mm.add(new MeasurementTrial(4.4, "MeasureTestID4", "Person4"));
        mm.add(new MeasurementTrial(5.5, "MeasureTestID5", "Person5"));
        return mm;
    }

    @Before
    public void setUp() {
        statistic = new Statistic();
        mmOddTrials = mockOddCountTrials();
        mmEvenTrials = mockEvenCountTrials();
        mmEmptyTrials = mockEmptyCountTrials();
    }

    @Test
    public void getTheDataTest() {
        ArrayList<Double> dataList = statistic.experimentData(mmOddTrials);

        for (int i = 0; i < mmOddTrials.size(); i++) {
            MeasurementTrial trial = (MeasurementTrial) mmOddTrials.get(i);
            assertEquals(trial.getMeasurement(), dataList.get(i));
        }
    }

    @Test
    public void measurementMeanTest() {
        double meanForOdd;
        double meanForEven;
        double meanForEmpty;

        meanForOdd = statistic.Mean(mmOddTrials);
        meanForEven = statistic.Mean(mmEvenTrials);
        meanForEmpty = statistic.Mean(mmEmptyTrials);

        assertEquals(3.3, meanForOdd);
        assertEquals(4.0, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void measurementMedianTest() {
        double medianForOdd;
        double medianForEven;
        double medianForEmpty;

        medianForOdd = statistic.Median(mmOddTrials);
        medianForEven = statistic.Median(mmEvenTrials);
        medianForEmpty = statistic.Median(mmEmptyTrials);

        assertEquals(3.3, medianForOdd);
        assertEquals(4.0, medianForEven);
        assertEquals(0.0, medianForEmpty);

    }

    @Test
    public void measurementStdDevTest() {
        double stdDevForOdd;
        double stdDevForEven;
        double stdDevForEmpty;

        stdDevForOdd = statistic.StdDev(mmOddTrials, statistic.Mean(mmOddTrials));
        stdDevForEven = statistic.StdDev(mmEvenTrials, statistic.Mean(mmEvenTrials));
        stdDevForEmpty = statistic.StdDev(mmEmptyTrials, statistic.Mean(mmEmptyTrials));

        assertEquals(0.0, stdDevForEmpty);
        assertEquals(1.556, stdDevForOdd);
        assertEquals(1.803, stdDevForEven);
    }

    @Test
    public void measurementQuartileTest() {
        double[] quartileForOdd;
        double[] quartileForEven;
        double[] quartileForEmpty;

        quartileForOdd = statistic.Quartiles(mmOddTrials);
        quartileForEven = statistic.Quartiles(mmEvenTrials);
        quartileForEmpty = statistic.Quartiles(mmEmptyTrials);

        assertEquals(1.65, quartileForOdd[0]);
        assertEquals(4.95, quartileForOdd[1]);
        assertEquals(2.5, quartileForEven[0]);
        assertEquals(5.5, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
    }
}
