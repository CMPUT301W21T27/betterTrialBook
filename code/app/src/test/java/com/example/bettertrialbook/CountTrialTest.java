
package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the CountTrial class
 */
public class CountTrialTest {
    CountTrial testTrial;
    Geolocation mockGeolocation;
    Date date;

    private CountTrial mockTrial() {
        return new CountTrial("testid", "testUser", mockGeolocation, date);
    }

    @Before
    public void setUp() {
        mockGeolocation = new Geolocation(new Location(""));
        date = Calendar.getInstance().getTime();
        testTrial = mockTrial();
    }

    @Test
    public void createTrialTest() {
        CountTrial comparisonTrial = new CountTrial("testid", "testUser", mockGeolocation, date);
        assertEquals(0, comparisonTrial.compareTo(testTrial));
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
