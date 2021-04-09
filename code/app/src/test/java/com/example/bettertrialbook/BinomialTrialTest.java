package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.Geolocation;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the BinomialTrial class
 */
public class BinomialTrialTest {
    BinomialTrial testTrial;
    Geolocation mockGeolocation;
    Date date;

    private BinomialTrial mockTrial() {
        return new BinomialTrial(true, "testid", "testUser", new Geolocation(new Location("")), date);
    }

    @Before
    public void setUp() {
        date = Calendar.getInstance().getTime();
        testTrial = mockTrial();
        mockGeolocation = new Geolocation(new Location(""));
    }

    @Test
    public void createTrialTest() {
        BinomialTrial comparisonTrial = new BinomialTrial(true, "testid", "testUser", mockGeolocation, date);
        assertEquals(0, comparisonTrial.compareTo(testTrial));
    }

    @Test
    public void getSuccessTest() {
        testTrial = mockTrial();
        assertTrue(testTrial.getSuccess());
    }

    @Test
    public void setSuccessTest() {
        testTrial.setSuccess(false);
        assertFalse(testTrial.getSuccess());
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
