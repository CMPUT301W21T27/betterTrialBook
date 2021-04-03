package com.example.bettertrialbook;

import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class HistogramInfoTest {

    private ArrayList<Double> result;
    private ArrayList<Double> dataSet;
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

    @Test
    public void testDistinctExperimentItem() {
        dataSet = mockDataSetOverFive();
        result = histogramInfo.distinctExperimentData(dataSet);

        assertEquals(7, result.size());
        assertEquals(1.0, result.get(0));
        assertEquals(2.0, result.get(1));
        assertEquals(3.0, result.get(2));
        assertEquals(4.0, result.get(3));
        assertEquals(5.0, result.get(4));
        assertEquals(6.0, result.get(5));
        assertEquals(7.0, result.get(6));
    }

    @Test
    public void testRangeOfData() {
        dataSet = mockDataSetOverFive();
        Double answer = histogramInfo.rangeOfData(dataSet);

        assertEquals(6.0, answer);
    }

    @Test
    public void testGetNumberofBins() {
        dataSet = mockDataSetOverFive();
        int answer = histogramInfo.getNumberofBins(dataSet);

        assertEquals(5, answer);
    }

    @Test
    public void testDifferenceForBins() {
        dataSet = mockDataSetOverFive();
        Double range = histogramInfo.rangeOfData(dataSet);
        int noOfBins = histogramInfo.getNumberofBins(dataSet);
        int answer = histogramInfo.differenceForBins(range, noOfBins);

        assertEquals(1, answer);
    }

    @Test
    public void testGetMaxForEachBin() {
        dataSet = mockDataSetOverFive();
        Double range = histogramInfo.rangeOfData(dataSet);
        int noOfBins = histogramInfo.getNumberofBins(dataSet);
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
        dataSet = mockDataSetOverFive();
        Double range = histogramInfo.rangeOfData(dataSet);
        int noOfBins = histogramInfo.getNumberofBins(dataSet);
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
    public void testCollectFrequency() {
        dataSet = mockDataSetOverFive();
        ArrayList<Integer> result = histogramInfo.collectFrequency(dataSet);

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
        dataSet = mockDataSetOverFive();
        ArrayList<String> labels = histogramInfo.getLabels(dataSet);

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
    }
}
