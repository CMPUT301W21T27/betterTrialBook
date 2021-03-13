package com.example.bettertrialbook;

import com.example.bettertrialbook.models.NonNegTrial;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NonNegTrialTest {
    NonNegTrial testTrial;

    private NonNegTrial mockTrial() {
        return new NonNegTrial(69, "testid");
    }

    @Before
    public void setUp() {
        testTrial = mockTrial();
    }

    @Test
    public void createTrialTest() {
        NonNegTrial comparisonTrial = new NonNegTrial(69, "testid");
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
}
