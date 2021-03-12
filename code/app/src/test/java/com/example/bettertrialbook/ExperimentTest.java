package com.example.bettertrialbook;

import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExperimentTest {
    ExperimentInfo testInfo;

    private ExperimentInfo mockInfo() {
        return new ExperimentInfo("abcdefg", "Test Info", "Active", "Binomial", false, 10, "Alberta");
    }

    @Before
    public void setUp() {
        testInfo = mockInfo();
    }

    @Test
    public void createExperimentTest() {
        ExperimentInfo comparisonInfo = new ExperimentInfo("abcdefg", "Test Info", "Active", "Binomial", false, 10, "Alberta");
        assertEquals(0, comparisonInfo.compareTo(testInfo));
    }

    @Test
    public void getIdTest() {
        assertEquals("abcdefg", testInfo.getId());
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Test Info", testInfo.getDescription());
    }

    @Test
    public void getStatusTest() {
        assertEquals("Active", testInfo.getStatus());
    }

    @Test
    public void getTrialTypeTest() {
        assertEquals("Binomial", testInfo.getTrialType());
    }

    @Test
    public void getGeoLocationRequiredTest() {
        assertEquals(false, testInfo.getGeoLocationRequired());
    }

    @Test
    public void getMinTrialsTest() {
        assertEquals(10, testInfo.getMinTrials());
    }

    @Test
    public void getRegionTest() {
        assertEquals("Alberta", testInfo.getRegion());
    }

    @Test
    public void setIdTest() {
        testInfo.setId("gfedcba");
        assertEquals("gfedcba", testInfo.getId());
    }
}
