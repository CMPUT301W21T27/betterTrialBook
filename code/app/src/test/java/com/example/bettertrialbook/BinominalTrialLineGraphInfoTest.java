package com.example.bettertrialbook;

import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.LineGraphInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class BinominalTrialLineGraphInfoTest {
    private LineGraphInfo lineGraph;
    private ArrayList<Trial> binomialTrials;
    private ArrayList<Trial> binomialEmptyTrials;

    private ArrayList<Trial> mockEmptyBinomialTrials() {
        ArrayList<Trial> binomial = new ArrayList<>();
        return binomial;
    }

    private ArrayList<Trial> mockBinomialTrials() {
        ArrayList<Trial> binomial = new ArrayList<>();

        binomial.add(new BinomialTrial(5, 9, "BinomialTestID1", "Person1"));
        binomial.add(new BinomialTrial(8, 3, "BinomialTestID2", "Person2"));

        return binomial;
    }

    @Before
    public void setUp() {
        lineGraph = new LineGraphInfo();
        binomialTrials = mockBinomialTrials();
        binomialEmptyTrials = mockEmptyBinomialTrials();
    }

    @Test
    public void testMeanOverTime() {
        ArrayList<Double> meanOverTime;
        ArrayList<Double> meanOverTimeEmpty;

        meanOverTime = lineGraph.MeanOverTime(binomialTrials);
        meanOverTimeEmpty = lineGraph.MeanOverTime(binomialEmptyTrials);

        // Result for non-empty arrayList
        assertEquals(0.357, meanOverTime.get(0));
        assertEquals(0.52, meanOverTime.get(1));
        // Result for empty arrayList
        assertNull(meanOverTimeEmpty);
    }

    @Test
    public void testStdDevOverTime() {
        ArrayList<Double> stdDevOverTime;
        ArrayList<Double> stdDevOverTimeEmpty;

        stdDevOverTime = lineGraph.StdDevOverTime(binomialTrials, lineGraph.MeanOverTime(binomialTrials));
        stdDevOverTimeEmpty = lineGraph.StdDevOverTime(binomialEmptyTrials, lineGraph.MeanOverTime(binomialEmptyTrials));

        // Result for non-empty arrayList
        assertEquals(0.479, stdDevOverTime.get(0));
        assertEquals(0.5, stdDevOverTime.get(1));
        // Result for empty arrayList
        assertNull(stdDevOverTimeEmpty);
    }

    @Test
    public void testMedianOverTime() {
        ArrayList<Double> medianOverTime;
        ArrayList<Double> medianOverTimeEmpty;

        medianOverTime = lineGraph.MedianOverTime(binomialTrials);
        medianOverTimeEmpty = lineGraph.MedianOverTime(binomialEmptyTrials);

        // Result for non-empty arrayList
        assertEquals(0.0, medianOverTime.get(0));
        assertEquals(1.0, medianOverTime.get(1));
        // Result for empty arrayList
        assertNull(medianOverTimeEmpty);
    }
}
