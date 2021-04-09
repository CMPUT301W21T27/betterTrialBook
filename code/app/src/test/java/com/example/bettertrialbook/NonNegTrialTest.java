package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.NonNegTrial;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the NonNegTrial class
 */
public class NonNegTrialTest {
    NonNegTrial testTrial;
    Geolocation mockGeolocation;
    Date date;

    private NonNegTrial mockTrial() {
        return new NonNegTrial(69, "testid", "testUser", new Geolocation(new Location("")), date);
    }

    @Before
    public void setUp() {
        date = Calendar.getInstance().getTime();
        testTrial = mockTrial();
        mockGeolocation = new Geolocation(new Location(""));
    }

    @Test
    public void createTrialTest() {
        NonNegTrial comparisonTrial = new NonNegTrial(69, "testid", "testUser", mockGeolocation, date);
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
        assertEquals("Non-Negative Integer", testTrial.getTrialType());
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
