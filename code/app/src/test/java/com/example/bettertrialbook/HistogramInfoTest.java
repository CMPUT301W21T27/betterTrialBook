/*
    Test Unit for HistogramInfo
 */
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.statistic.HistogramInfo;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertEquals;

public class HistogramInfoTest {
    HistogramInfo histogramInfo = new HistogramInfo();
    private ArrayList<Double> dataList1;
    private ArrayList<Double> dataList2;
    private ArrayList<Double> dataList3;

    private ArrayList<Double> mockDataListSmallerThan5() {
        ArrayList<Double> data = new ArrayList<>();

        data.add(1.0);
        data.add(3.0);
        data.add(3.0);
        data.add(3.0);
        data.add(5.0);
        data.add(5.0);

        return data;
    }

    private ArrayList<Double> mockDataListLargerOrEqualTo5() {
        ArrayList<Double> data = new ArrayList<>();

        data.add(1.0);
        data.add(3.0);
        data.add(3.0);
        data.add(3.0);
        data.add(5.0);
        data.add(5.0);
        data.add(6.0);
        data.add(7.0);

        return data;
    }

    @Before
    public void setUp() {
        dataList1 = mockDataListSmallerThan5();
        dataList2 = mockDataListLargerOrEqualTo5();
        dataList3 = new ArrayList<>();
    }

    @Test
    public void testCollectFrequency() {
        ArrayList<Integer> frequencies1 = histogramInfo.collectFrequency(dataList1);
        ArrayList<Integer> frequencies2 = histogramInfo.collectFrequency(dataList2);
        ArrayList<Integer> frequencies3 = histogramInfo.collectFrequency(dataList3);

        // Results for dataList1 (Smaller Than 5 and Larger than 0)
        assertEquals(1, (int) frequencies1.get(0));
        assertEquals(3, (int) frequencies1.get(1));
        assertEquals(2, (int) frequencies1.get(2));

        // Results for dataList2 (Larger or Equal to 5)
        assertEquals(1, (int) frequencies2.get(0));
        assertEquals(3, (int) frequencies2.get(1));
        assertEquals(2, (int) frequencies2.get(2));
        assertEquals(2, (int) frequencies2.get(3));
        assertEquals(0, (int) frequencies2.get(4));

        // Results for dataList3 (Empty)
        assertNull(frequencies3);
    }

    @Test
    public void testGetLabels() {
        ArrayList<String> labels1 = histogramInfo.getLabels(dataList1);
        ArrayList<String> labels2 = histogramInfo.getLabels(dataList2);
        ArrayList<String> labels3 = histogramInfo.getLabels(dataList3);

        // Results for dataList1 (Smaller than 5 and larger than 0)
        assertEquals("1.0", labels1.get(0));
        assertEquals("3.0", labels1.get(1));
        assertEquals("5.0", labels1.get(2));

        // Results for dataList2 (Larger than 5)
        assertEquals("0-1", labels2.get(0));
        assertEquals("2-3", labels2.get(1));
        assertEquals("4-5", labels2.get(2));
        assertEquals("6-7", labels2.get(3));
        assertEquals("8+", labels2.get(4));

        // Results for dataList3 (Empty)
        assertNull(labels3);
    }

    //-------------------------- Special Test for Count Trial Type ---------------------------------
    @Test
    public void testGetNumberOfBinsCountTrial() {
        ArrayList<Trial> countTrial1 = new ArrayList<>();
        ArrayList<Trial> countTrial2 = new ArrayList<>();

        countTrial1.add(new CountTrial("1", "Abby", new Geolocation(new Location("")), new Date()));
        countTrial1.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial1.add(new CountTrial("3", "Terence2", new Geolocation(new Location("")), new Date()));
        countTrial1.add(new CountTrial("4", "Terence2", new Geolocation(new Location("")), new Date()));

        ArrayList<String> names1 = histogramInfo.getDistinctExperimenter(countTrial1);
        ArrayList<String> names2 = histogramInfo.getDistinctExperimenter(countTrial2);

        // Results for non-empty arrayList of Trial
        assertEquals(3, names1.size());
        assertEquals("Abby", names1.get(0));
        assertEquals("Terence", names1.get(1));
        assertEquals("Terence2", names1.get(2));

        // Results for empty arrayList of Trial
        assertNull(names2);
    }

    @Test
    public void testCollectFrequencyCountTrial() {
        ArrayList<Trial> countTrial1 = new ArrayList<>();
        ArrayList<Trial> countTrial2 = new ArrayList<>();

        countTrial1.add(new CountTrial("1", "Abby", new Geolocation(new Location("")), new Date()));
        countTrial1.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial1.add(new CountTrial("3", "Terence2", new Geolocation(new Location("")), new Date()));
        countTrial1.add(new CountTrial("4", "Terence2", new Geolocation(new Location("")), new Date()));

        ArrayList<Integer> frequency1 = histogramInfo.collectFrequencyCountTrial(countTrial1);
        ArrayList<Integer> frequency2 = histogramInfo.collectFrequencyCountTrial(countTrial2);

        // Results for non-empty arrayList of Trial
        assertEquals(1, (int) frequency1.get(0));
        assertEquals(1, (int) frequency1.get(1));
        assertEquals(2, (int) frequency1.get(2));

        // Results for empty arrayList of Trial
        assertNull(frequency2);
    }
}