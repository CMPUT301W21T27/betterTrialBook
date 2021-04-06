/*
Unit tests for the BinomialTrial class
 */

package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.Geolocation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinomialTrialTest {
    BinomialTrial testTrial;
    Geolocation mockGeolocation;

    private BinomialTrial mockTrial() {
        return new BinomialTrial(6, 9, "testid", "testUser", new Geolocation(new Location("")));
    }

    @Before
    public void setUp() {
        testTrial = mockTrial();
        mockGeolocation = new Geolocation(new Location(""));
    }

    @Test
    public void createTrialTest() {
        BinomialTrial comparisonTrial = new BinomialTrial(6, 9, "testid", "testUser", mockGeolocation);
        assertEquals(0, comparisonTrial.compareTo(testTrial));
    }

    @Test
    public void getPassCountTest() {
        testTrial = mockTrial();
        assertEquals(6, testTrial.getPassCount());
    }

    @Test
    public void setPassCountTest() {
        testTrial.setPassCount(1);
        assertEquals(1, testTrial.getPassCount());
    }

    @Test
    public void getFailCountTest() {
        testTrial = mockTrial();
        assertEquals(9, testTrial.getFailCount());
    }

    @Test
    public void setFailCountTest() {
        testTrial.setFailCount(1);
        assertEquals(1, testTrial.getFailCount());
    }

    @Test
    public void getTrialTypeTest() {
        testTrial = mockTrial();
        assertEquals("Binomial", testTrial.getTrialType());
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
