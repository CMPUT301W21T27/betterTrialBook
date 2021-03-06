package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.statistic.LineGraphInfo;

import org.junit.Test;
import org.junit.Before;

import java.util.Date;
import java.util.ArrayList;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertEquals;

/**
 * Test Unit for LineGraph.java (Count-Based trial)
 *     Remarks:
 *     1) Since mean, median and std dev is meaningless in Count-Based trial
 *        Count-Based Trial will not use any methods related to the mean, median and std dev
 *        in the lineGraphInfo.java
 */
public class CountTrialLineGraphInfoTest {
    private ArrayList<Trial> countTrial;
    private ArrayList<Trial> countTrialEmpty;
    private LineGraphInfo lineGraphInfo1;
    private LineGraphInfo lineGraphInfo2;


    private ArrayList<Trial> mockCountTrialEmpty() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        return countTrial;
    }

    private ArrayList<Trial> mockCountTrial() {
        ArrayList<Trial> countTrial = new ArrayList<>();

        countTrial.add(new CountTrial("1", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("2", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("3", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("4", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("5", "Terence", new Geolocation(new Location("")), new Date()));
        countTrial.add(new CountTrial("6", "Terence", new Geolocation(new Location("")), new Date()));

        return countTrial;
    }

    @Before
    public void setUp() {
        countTrial = mockCountTrial();
        countTrialEmpty = mockCountTrialEmpty();

        lineGraphInfo1 = new LineGraphInfo(countTrial, Extras.COUNT_TYPE);
        lineGraphInfo2 = new LineGraphInfo(countTrialEmpty, Extras.COUNT_TYPE);
    }

    @Test
    public void testResultOverTime() {
        ArrayList<Double> result1 = lineGraphInfo1.ResultOverTime();
        ArrayList<Double> result2 = lineGraphInfo2.ResultOverTime();
        assertEquals(6.0, result1.get(0));
        assertNull(result2);
    }
}
