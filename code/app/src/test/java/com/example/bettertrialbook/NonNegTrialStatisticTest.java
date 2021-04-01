/* All the expected value calculation are done by hand
 * Round Up to 3 decimal places
 */
package com.example.bettertrialbook;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.NonNegTrial;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class NonNegTrialStatisticTest {
    private Statistic statistic;
    private ArrayList<Trial> nonNegOddTrials;
    private ArrayList<Trial> nonNegEvenTrials;
    private ArrayList<Trial> nonNegEmptyTrials;

    private ArrayList<Trial> mockEmptyCountTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        return nonNeg;
    }

    private ArrayList<Trial> mockEvenCountTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1"));
        nonNeg.add(new NonNegTrial(2, "NonNegTestID2", "Person2"));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3"));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID4", "Person4"));
        return nonNeg;
    }

    private ArrayList<Trial> mockOddCountTrials() {
        ArrayList<Trial> nonNeg = new ArrayList<>();
        nonNeg.add(new NonNegTrial(1, "NonNegTestID1", "Person1"));
        nonNeg.add(new NonNegTrial(2, "NonNegTestID2", "Person2"));
        nonNeg.add(new NonNegTrial(3, "NonNegTestID3", "Person3"));
        nonNeg.add(new NonNegTrial(4, "NonNegTestID4", "Person4"));
        nonNeg.add(new NonNegTrial(5, "NonNegTestID5", "Person5"));
        return nonNeg;
    }

    @Before
    public void setUp() {
        statistic = new Statistic();
        nonNegOddTrials = mockOddCountTrials();
        nonNegEvenTrials = mockEvenCountTrials();
        nonNegEmptyTrials = mockEmptyCountTrials();
    }

    @Test
    public void NonNegMeanTest() {
        double meanForOdd;
        double meanForEven;
        double meanForEmpty;

        meanForOdd = statistic.Mean(nonNegOddTrials);
        meanForEven = statistic.Mean(nonNegEvenTrials);
        meanForEmpty = statistic.Mean(nonNegEmptyTrials);

        assertEquals(3.0, meanForOdd);
        assertEquals(2.5, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void NonNegMedianTest() {
        double medianForOdd;
        double medianForEven;
        double medianForEmpty;

        medianForOdd = statistic.Median(nonNegOddTrials);
        medianForEven = statistic.Median(nonNegEvenTrials);
        medianForEmpty = statistic.Median(nonNegEmptyTrials);

        assertEquals(3.0, medianForOdd);
        assertEquals(2.5, medianForEven);
        assertEquals(0.0, medianForEmpty);

    }

    @Test
    public void NonNegStdDevTest() {
        double stdDevForOdd;
        double stdDevForEven;
        double stdDevForEmpty;

        stdDevForOdd = statistic.StdDev(nonNegOddTrials, statistic.Mean(nonNegOddTrials));
        stdDevForEven = statistic.StdDev(nonNegEvenTrials, statistic.Mean(nonNegEvenTrials));
        stdDevForEmpty = statistic.StdDev(nonNegEmptyTrials, statistic.Mean(nonNegEmptyTrials));

        assertEquals(0.0, stdDevForEmpty);
        assertEquals(1.414, stdDevForOdd);
        assertEquals(1.118, stdDevForEven);
    }

    @Test
    public void NonNegQuartileTest() {
        double[] quartileForOdd;
        double[] quartileForEven;
        double[] quartileForEmpty;

        quartileForOdd = statistic.Quartiles(nonNegOddTrials);
        quartileForEven = statistic.Quartiles(nonNegEvenTrials);
        quartileForEmpty = statistic.Quartiles(nonNegEmptyTrials);

        assertEquals(1.5, quartileForOdd[0]);
        assertEquals(4.5, quartileForOdd[1]);
        assertEquals(1.5, quartileForEven[0]);
        assertEquals(3.5, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
    }
}
