/* All the expected value calculation are done by hand
 * Round Up to 3 decimal places
 */
package com.example.bettertrialbook;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.statistic.Statistic;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class CountTrialStatisticTest {
    private Statistic statistic;
    private ArrayList<Trial> countOddTrials;
    private ArrayList<Trial> countEvenTrials;
    private ArrayList<Trial> countEmptyTrials;

    private ArrayList<Trial> mockEmptyCountTrials() {
        ArrayList<Trial> counts = new ArrayList<>();
        return counts;
    }

    private ArrayList<Trial> mockEvenCountTrials() {
        ArrayList<Trial> counts = new ArrayList<>();
        counts.add(new CountTrial(1, "CountTestID1", "Person1"));
        counts.add(new CountTrial(2, "CountTestID2", "Person2"));
        counts.add(new CountTrial(3, "CountTestID3", "Person3"));
        counts.add(new CountTrial(4, "CountTestID4", "Person4"));
        counts.add(new CountTrial(5, "CountTestID5", "Person5"));
        counts.add(new CountTrial(6, "CountTestID6", "Person6"));
        return counts;
    }

    private ArrayList<Trial> mockOddCountTrials() {
        ArrayList<Trial> counts = new ArrayList<>();
        counts.add(new CountTrial(1, "CountTestID1", "Person1"));
        counts.add(new CountTrial(2, "CountTestID2", "Person2"));
        counts.add(new CountTrial(3, "CountTestID3", "Person3"));
        counts.add(new CountTrial(4, "CountTestID4", "Person4"));
        counts.add(new CountTrial(5, "CountTestID5", "Person5"));
        return counts;
    }

    @Before
    public void setUp() {
        statistic = new Statistic();
        countOddTrials = mockOddCountTrials();
        countEvenTrials = mockEvenCountTrials();
        countEmptyTrials = mockEmptyCountTrials();
    }

    @Test
    public void getTheDataTest() {
        ArrayList<Double> data = statistic.experimentData(countOddTrials);

        for (int i = 0; i < countOddTrials.size(); i++) {
            CountTrial trial = (CountTrial) countOddTrials.get(i);
            assertEquals(trial.getCount(), data.get(i).intValue());
        }
    }

    @Test
    public void countMeanTest() {
        double meanForOdd;
        double meanForEven;
        double meanForEmpty;

        meanForOdd = statistic.Mean(countOddTrials);
        meanForEven = statistic.Mean(countEvenTrials);
        meanForEmpty = statistic.Mean(countEmptyTrials);

        assertEquals(3.0, meanForOdd);
        assertEquals(3.5, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void countMedianTest() {
        double medianForOdd;
        double medianForEven;
        double medianForEmpty;

        medianForOdd = statistic.Median(countOddTrials);
        medianForEven = statistic.Median(countEvenTrials);
        medianForEmpty = statistic.Median(countEmptyTrials);

        assertEquals(3.0, medianForOdd);
        assertEquals(3.5, medianForEven);
        assertEquals(0.0, medianForEmpty);

    }

    @Test
    public void countStdDevTest() {
        double stdDevForOdd;
        double stdDevForEven;
        double stdDevForEmpty;

        stdDevForOdd = statistic.StdDev(countOddTrials, statistic.Mean(countOddTrials));
        stdDevForEven = statistic.StdDev(countEvenTrials, statistic.Mean(countEvenTrials));
        stdDevForEmpty = statistic.StdDev(countEmptyTrials, statistic.Mean(countEmptyTrials));

        assertEquals(0.0, stdDevForEmpty);
        assertEquals(1.414, stdDevForOdd);
        assertEquals(1.708, stdDevForEven);
    }

    @Test
    public void countQuartileTest() {
        double[] quartileForOdd;
        double[] quartileForEven;
        double[] quartileForEmpty;

        quartileForOdd = statistic.Quartiles(countOddTrials);
        quartileForEven = statistic.Quartiles(countEvenTrials);
        quartileForEmpty = statistic.Quartiles(countEmptyTrials);

        assertEquals(1.5, quartileForOdd[0]);
        assertEquals(4.5, quartileForOdd[1]);
        assertEquals(2.0, quartileForEven[0]);
        assertEquals(5.0, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
    }
}
