package com.example.bettertrialbook;

import com.example.bettertrialbook.models.MeasurementTrial;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MeasurementTrialTest {
    MeasurementTrial testTrial;

    private MeasurementTrial mockTrial() {
        return new MeasurementTrial(420.69, "testid", "testUser");
    }

    @Before
    public void setUp() {
        testTrial = mockTrial();
    }

    @Test
    public void createTrialTest() {
        MeasurementTrial comparisonTrial = new MeasurementTrial(420.69, "testid", "testUser");
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
}
