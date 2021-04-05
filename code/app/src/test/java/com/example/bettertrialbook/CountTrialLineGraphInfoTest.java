package com.example.bettertrialbook;

import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.LineGraphInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class CountTrialLineGraphInfoTest {
    private LineGraphInfo lineGraphInfo;
    private ArrayList<Trial> countOddTrials;
    private ArrayList<Trial> countEvenTrials;
    private ArrayList<Trial> countEmptyTrials;

    private ArrayList<Trial> mockEmptyCountTrials() {
        return new ArrayList<Trial>();
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
        lineGraphInfo = new LineGraphInfo();
        countOddTrials = mockOddCountTrials();
        countEvenTrials = mockEvenCountTrials();
        countEmptyTrials = mockEmptyCountTrials();
    }

    @Test
    public void testMeanOvertime() {
        ArrayList<Double> meanOverTimeOdd;
        ArrayList<Double> meanOverTimeEven;
        ArrayList<Double> meanOverTimeEmpty;

        meanOverTimeOdd = lineGraphInfo.MeanOverTime(countOddTrials);
        meanOverTimeEven = lineGraphInfo.MeanOverTime(countEvenTrials);
        meanOverTimeEmpty = lineGraphInfo.MeanOverTime(countEmptyTrials);

        // Result for odd ArrayList
        assertEquals(1.0, meanOverTimeOdd.get(0));
        assertEquals(1.5, meanOverTimeOdd.get(1));
        assertEquals(2.0, meanOverTimeOdd.get(2));
        assertEquals(2.5, meanOverTimeOdd.get(3));
        assertEquals(3.0, meanOverTimeOdd.get(4));
        // Result for even ArrayList
        assertEquals(1.0, meanOverTimeEven.get(0));
        assertEquals(1.5, meanOverTimeEven.get(1));
        assertEquals(2.0, meanOverTimeEven.get(2));
        assertEquals(2.5, meanOverTimeEven.get(3));
        assertEquals(3.0, meanOverTimeEven.get(4));
        assertEquals(3.5, meanOverTimeEven.get(5));
        // Result for empty ArrayList
        assertNull(meanOverTimeEmpty);
    }

    @Test
    public void testStdDevOverTime() {
        ArrayList<Double> stdDevOverTimeOdd;
        ArrayList<Double> stdDevOverTimeEven;
        ArrayList<Double> stdDevOverTimeEmpty;

        stdDevOverTimeOdd = lineGraphInfo.StdDevOverTime(countOddTrials, lineGraphInfo.MeanOverTime(countOddTrials));
        stdDevOverTimeEven = lineGraphInfo.StdDevOverTime(countEvenTrials, lineGraphInfo.MeanOverTime(countEvenTrials));
        stdDevOverTimeEmpty = lineGraphInfo.StdDevOverTime(countEmptyTrials, lineGraphInfo.MeanOverTime(countEmptyTrials));

        // Result for the Odd ArrayList
        assertEquals(0.0, stdDevOverTimeOdd.get(0));
        assertEquals(0.5, stdDevOverTimeOdd.get(1));
        assertEquals(0.816, stdDevOverTimeOdd.get(2));
        assertEquals(1.118, stdDevOverTimeOdd.get(3));
        assertEquals(1.414, stdDevOverTimeOdd.get(4));
        // Result for the Even ArrayList
        assertEquals(0.0, stdDevOverTimeEven.get(0));
        assertEquals(0.5, stdDevOverTimeEven.get(1));
        assertEquals(0.816, stdDevOverTimeEven.get(2));
        assertEquals(1.118, stdDevOverTimeEven.get(3));
        assertEquals(1.414, stdDevOverTimeEven.get(4));
        assertEquals(1.708, stdDevOverTimeEven.get(5));
        // Result for the Empty ArrayList
        assertNull(stdDevOverTimeEmpty);
    }

    @Test
    public void testMedianOverTime() {
        ArrayList<Double> medianOverTimeOdd;
        ArrayList<Double> medianOverTimeEven;
        ArrayList<Double> medianOverTimeEmpty;

        medianOverTimeOdd = lineGraphInfo.MedianOverTime(countOddTrials);
        medianOverTimeEven = lineGraphInfo.MedianOverTime(countEvenTrials);
        medianOverTimeEmpty = lineGraphInfo.MedianOverTime(countEmptyTrials);

        // Results for Odd ArrayList
        assertEquals(1.0, medianOverTimeOdd.get(0));
        assertEquals(1.5, medianOverTimeOdd.get(1));
        assertEquals(2.0, medianOverTimeOdd.get(2));
        assertEquals(2.5, medianOverTimeOdd.get(3));
        assertEquals(3.0, medianOverTimeOdd.get(4));
        // Results for Even ArrayList
        assertEquals(1.0, medianOverTimeEven.get(0));
        assertEquals(1.5, medianOverTimeEven.get(1));
        assertEquals(2.0, medianOverTimeEven.get(2));
        assertEquals(2.5, medianOverTimeEven.get(3));
        assertEquals(3.0, medianOverTimeEven.get(4));
        assertEquals(3.5, medianOverTimeEven.get(5));
        // Results for Empty ArrayList
        assertNull(medianOverTimeEmpty);
    }
}
