package com.example.bettertrialbook;

import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class HistogramInfoTest {

    /*  Remarks:
     *  The ArrayList<Double> data passed in is a sorted ArrayList.
     */

    private ArrayList<Double> result1;
    private ArrayList<Double> result2;
    private ArrayList<Double> dataSet1;
    private ArrayList<Double> dataSet2;

    HistogramInfo histogramInfo = new HistogramInfo();

    private ArrayList<Double> mockDataSetOverFive() {
        ArrayList<Double> data = new ArrayList<>();
        data.add(1.0);
        data.add(2.0);
        data.add(3.0);
        data.add(3.0);
        data.add(3.0);
        data.add(4.0);
        data.add(5.0);
        data.add(5.0);
        data.add(6.0);
        data.add(7.0);

        return data;
    }

    private ArrayList<Double> mockDataSetBelowFive() {
        ArrayList<Double> data = new ArrayList<>();
        data.add(0.0);
        data.add(0.0);
        data.add(0.0);
        data.add(1.0);
        data.add(1.0);
        data.add(1.0);
        data.add(1.0);
        data.add(1.0);
        data.add(1.0);

        return data;
    }

    @Before
    public void setUp() {
        dataSet1 = mockDataSetOverFive();
        dataSet2 = mockDataSetBelowFive();
    }

    @Test
    public void testDistinctExperimentItem() {
        result1 = histogramInfo.distinctExperimentData(dataSet1);
        result2 = histogramInfo.distinctExperimentData(dataSet2);
        // Result related to more than or 5 distinct elements in the dataset
        assertEquals(7, result1.size());
        assertEquals(1.0, result1.get(0));
        assertEquals(2.0, result1.get(1));
        assertEquals(3.0, result1.get(2));
        assertEquals(4.0, result1.get(3));
        assertEquals(5.0, result1.get(4));
        assertEquals(6.0, result1.get(5));
        assertEquals(7.0, result1.get(6));
        // Result related to less than 5 distinct elements in the dataset
        assertEquals(2, result2.size());
        assertEquals(0.0, result2.get(0));
        assertEquals(1.0, result2.get(1));
    }

    @Test
    public void testRangeOfData() {
        double answer1 = histogramInfo.rangeOfData(dataSet1);
        double answer2 = histogramInfo.rangeOfData(dataSet2);

        assertEquals(6.0, answer1);
        assertEquals(1.0, answer2);
    }

    @Test
    public void testGetNumberofBins() {
        int answer1 = histogramInfo.getNumberofBins(dataSet1);
        int answer2 = histogramInfo.getNumberofBins(dataSet2);

        assertEquals(5, answer1);
        assertEquals(2, answer2);
    }

    @Test
    public void testDifferenceForBins() {
        double range = histogramInfo.rangeOfData(dataSet1);
        int noOfBins = histogramInfo.getNumberofBins(dataSet1);
        int answer = histogramInfo.differenceForBins(range, noOfBins);

        assertEquals(1, answer);
    }

    @Test
    public void testGetCategory() {
        int noOfBins = histogramInfo.getNumberofBins(dataSet2);
        double[] answer = histogramInfo.getCategory(dataSet2, noOfBins);

        assertEquals(0.0, answer[0]);
        assertEquals(1.0, answer[1]);
    }

    @Test
    public void testGetBinFrequencyB() {
        int noOfBins = histogramInfo.getNumberofBins(dataSet2);
        ArrayList<Integer> result = histogramInfo.getBinFrequencyB(dataSet2, noOfBins);

        int answer1 = result.get(0);
        int answer2 = result.get(1);

        assertEquals(3, answer1);
        assertEquals(6, answer2);
    }

    @Test
    public void testGetMaxForEachBin() {
        Double range = histogramInfo.rangeOfData(dataSet1);
        int noOfBins = histogramInfo.getNumberofBins(dataSet1);
        int diffBins = histogramInfo.differenceForBins(range, noOfBins);
        int[] maxForEachBin = histogramInfo.getMaxForEachBin(diffBins, noOfBins);

        assertEquals(1, maxForEachBin[0]);
        assertEquals(3, maxForEachBin[1]);
        assertEquals(5, maxForEachBin[2]);
        assertEquals(7, maxForEachBin[3]);
        assertEquals(9, maxForEachBin[4]);
    }

    @Test
    public void testGetMinForEachBin() {
        Double range = histogramInfo.rangeOfData(dataSet1);
        int noOfBins = histogramInfo.getNumberofBins(dataSet1);
        int diffBins = histogramInfo.differenceForBins(range, noOfBins);
        int[] maxForEachBin = histogramInfo.getMaxForEachBin(diffBins, noOfBins);
        int[] minForEachBin = histogramInfo.getMinForEachBin(diffBins, maxForEachBin, noOfBins);

        assertEquals(0, minForEachBin[0]);
        assertEquals(2, minForEachBin[1]);
        assertEquals(4, minForEachBin[2]);
        assertEquals(6, minForEachBin[3]);
        assertEquals(8, minForEachBin[4]);
    }

    @Test
    public void testGetBinFrequencyA() {
        ArrayList<Integer> result = histogramInfo.collectFrequency(dataSet1);

        int frequency1 = result.get(0);
        int frequency2 = result.get(1);
        int frequency3 = result.get(2);
        int frequency4 = result.get(3);
        int frequency5 = result.get(4);
        assertEquals(1, frequency1);
        assertEquals(4, frequency2);
        assertEquals(3, frequency3);
        assertEquals(2, frequency4);
        assertEquals(0, frequency5);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> labels = histogramInfo.getLabels(dataSet1);

        String label1 = labels.get(0);
        String label2 = labels.get(1);
        String label3 = labels.get(2);
        String label4 = labels.get(3);
        String label5 = labels.get(4);
        assertEquals("0-1", label1);
        assertEquals("2-3", label2);
        assertEquals("4-5", label3);
        assertEquals("6-7", label4);
        assertEquals("8-9", label5);

        ArrayList<String> labelS = histogramInfo.getLabels(dataSet2);

        String label6 = labelS.get(0);
        String label7 = labelS.get(1);

        assertEquals("0.0", label6);
        assertEquals("1.0", label7);
    }
}
