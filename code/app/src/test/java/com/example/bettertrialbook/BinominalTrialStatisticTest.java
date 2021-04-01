/* All the expected value calculation are done by hand
 * Round Up to 3 decimal places
 */
package com.example.bettertrialbook;

import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.Trial;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class BinominalTrialStatisticTest {
    private Statistic statistic;
    private ArrayList<Trial> binomialOddTrials;
    private ArrayList<Trial> binomialEvenTrials;
    private ArrayList<Trial> binomialEmptyTrials;

    private ArrayList<Trial> mockEmptyCountTrials() {
        ArrayList<Trial> mm = new ArrayList<>();
        return mm;
    }

    private ArrayList<Trial> mockEvenCountTrials() {
        ArrayList<Trial> binomial = new ArrayList<>();

        binomial.add(new BinomialTrial(5, 9, "BinomialTestID1", "Person1"));

        return binomial;
    }

    private ArrayList<Trial> mockOddCountTrials() {
        ArrayList<Trial> binomial = new ArrayList<>();

        binomial.add(new BinomialTrial(8, 3, "BinomialTestID2", "Person2"));

        return binomial;
    }

    @Before
    public void setUp() {
        statistic = new Statistic();
        binomialOddTrials = mockOddCountTrials();
        binomialEvenTrials = mockEvenCountTrials();
        binomialEmptyTrials = mockEmptyCountTrials();
    }

    @Test
    public void BinomialgMeanTest() {
        double meanForOdd;
        double meanForEven;
        double meanForEmpty;

        meanForOdd = statistic.Mean(binomialOddTrials);
        meanForEven = statistic.Mean(binomialEvenTrials);
        meanForEmpty = statistic.Mean(binomialEmptyTrials);

        assertEquals(0.727, meanForOdd);
        assertEquals(0.357, meanForEven);
        assertEquals(0.0, meanForEmpty);
    }

    @Test
    public void BinomialMedianTest() {
        double medianForOdd;
        double medianForEven;
        double medianForEmpty;

        medianForOdd = statistic.Median(binomialOddTrials);
        medianForEven = statistic.Median(binomialEvenTrials);
        medianForEmpty = statistic.Median(binomialEmptyTrials);

        assertEquals(1.0, medianForOdd);
        assertEquals(0.0, medianForEven);
        assertEquals(0.0, medianForEmpty);

    }

    @Test
    public void BinomialStdDevTest() {
        double stdDevForOdd;
        double stdDevForEven;
        double stdDevForEmpty;

        stdDevForOdd = statistic.StdDev(binomialOddTrials, statistic.Mean(binomialOddTrials));
        stdDevForEven = statistic.StdDev(binomialEvenTrials, statistic.Mean(binomialEvenTrials));
        stdDevForEmpty = statistic.StdDev(binomialEmptyTrials, statistic.Mean(binomialEmptyTrials));

        assertEquals(0.0, stdDevForEmpty);
        assertEquals(0.445, stdDevForOdd);
        assertEquals(0.479, stdDevForEven);
    }

    @Test
    public void BinomialQuartileTest() {
        double[] quartileForOdd;
        double[] quartileForEven;
        double[] quartileForEmpty;

        quartileForOdd = statistic.Quartiles(binomialOddTrials);
        quartileForEven = statistic.Quartiles(binomialEvenTrials);
        quartileForEmpty = statistic.Quartiles(binomialEmptyTrials);

        assertEquals(0.0, quartileForOdd[0]);
        assertEquals(1.0, quartileForOdd[1]);
        assertEquals(0.0, quartileForEven[0]);
        assertEquals(1.0, quartileForEven[1]);
        assertEquals(0.0, quartileForEmpty[0]);
        assertEquals(0.0, quartileForEmpty[1]);
    }
}
