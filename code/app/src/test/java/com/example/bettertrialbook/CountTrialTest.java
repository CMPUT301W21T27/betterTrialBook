/*
Unit tests for the CountTrial class
 */

package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountTrialTest {
    CountTrial testTrial;
    Geolocation mockGeolocation;

    private CountTrial mockTrial() {
        return new CountTrial(69, "testid", "testUser", new Geolocation(new Location("")));
    }

    @Before
    public void setUp() {
        testTrial = mockTrial();
        mockGeolocation = new Geolocation(new Location(""));
    }

    @Test
    public void createTrialTest() {
        CountTrial comparisonTrial = new CountTrial(69, "testid", "testUser", mockGeolocation);
        assertEquals(0, comparisonTrial.compareTo(testTrial));
    }

    @Test
    public void getCountTest() {
        testTrial = mockTrial();
        assertEquals(69, testTrial.getCount());
    }

    @Test
    public void seCountTest() {
        testTrial.setCount(1);
        assertEquals(1, testTrial.getCount());
    }

    @Test
    public void getTrialTypeTest() {
        testTrial = mockTrial();
        assertEquals("Count-Based", testTrial.getTrialType());
    }

    @Test
    public void getTrialIdTest() {
        testTrial = mockTrial();
        assertEquals("testid", testTrial.getTrialID());
    }

    @Test
    public void setTrialIdTest() {
        testTrial.setTrialID("newtestid");
        assertEquals("newtestid", testTrial.getTrialID());
    }

    @Test
    public void getGeolocationTest() {
        testTrial = mockTrial();
        assertEquals(mockGeolocation, testTrial.getGeolocation());
    }

    @Test
    public void setGeolocationTest() {
        testTrial.setGeolocation(mockGeolocation);
        assertEquals(mockGeolocation, testTrial.getGeolocation());
    }
}
