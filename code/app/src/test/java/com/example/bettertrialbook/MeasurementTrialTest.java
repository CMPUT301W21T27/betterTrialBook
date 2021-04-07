/*
Unit tests for the MeasurementTrial class
 */

package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.MeasurementTrial;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MeasurementTrialTest {
    MeasurementTrial testTrial;
    Geolocation mockGeolocation;
    Date date;

    private MeasurementTrial mockTrial() {
        return new MeasurementTrial(420.69, "testid", "testUser", new Geolocation(new Location("")), date);
    }

    @Before
    public void setUp() {
        date = Calendar.getInstance().getTime();
        testTrial = mockTrial();
        mockGeolocation = new Geolocation(new Location(""));
    }

    @Test
    public void createTrialTest() {
        MeasurementTrial comparisonTrial = new MeasurementTrial(420.69, "testid", "testUser", mockGeolocation, date);
        assertEquals(0, comparisonTrial.compareTo(testTrial));
    }

    @Test
    public void getMeasurementTest() {
        testTrial = mockTrial();
        assertEquals(Double.valueOf(420.69), testTrial.getMeasurement());
    }

    @Test
    public void setMeasurementTest() {
        testTrial.setMeasurement(1.01);
        assertEquals(Double.valueOf(1.01), testTrial.getMeasurement());
    }

    @Test
    public void getTrialTypeTest() {
        testTrial = mockTrial();
        assertEquals("Measurement", testTrial.getTrialType());
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
