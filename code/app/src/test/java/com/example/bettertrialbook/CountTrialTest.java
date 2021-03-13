package com.example.bettertrialbook;

import com.example.bettertrialbook.models.CountTrial;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountTrialTest {
    CountTrial testTrial;

    private CountTrial mockTrial() {
        return new CountTrial(69, "testid");
    }

    @Before
    public void setUp() {
        testTrial = mockTrial();
    }

    @Test
    public void createTrialTest() {
        CountTrial comparisonTrial = new CountTrial(69, "testid");
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
}
