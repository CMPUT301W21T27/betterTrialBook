package com.example.bettertrialbook;

import com.example.bettertrialbook.models.BinomialTrial;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinomialTrialTest {
    BinomialTrial testTrial;

    private BinomialTrial mockTrial() {
        return new BinomialTrial(6, 9, "testid", "testUser");
    }

    @Before
    public void setUp() {
        testTrial = mockTrial();
    }

    @Test
    public void createTrialTest() {
        BinomialTrial comparisonTrial = new BinomialTrial(6, 9, "testid", "testUser");
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
}
