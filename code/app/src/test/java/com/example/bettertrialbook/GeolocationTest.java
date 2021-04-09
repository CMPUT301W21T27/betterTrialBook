package com.example.bettertrialbook;

import android.location.Location;

import com.example.bettertrialbook.models.Geolocation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Geolocation class
 */
public class GeolocationTest {
    Geolocation testGeolocation;
    Location mockLocation;


    private Geolocation mockGeolocation() {
        return new Geolocation(mockLocation);
    }

    @Before
    public void setUp() {
        mockLocation = new Location("");
        mockLocation.setLatitude(10);
        mockLocation.setLongitude(10);
        testGeolocation = mockGeolocation();
    }

    @Test
    public void createGeolocationTest() {
        Geolocation comparisonGeolocation = new Geolocation(mockLocation);
        assertEquals(0, comparisonGeolocation.compareTo(testGeolocation));
    }

    @Test
    public void getLocationTest() {
        testGeolocation = mockGeolocation();
        assertEquals(mockLocation, testGeolocation.getLocation());
    }

    @Test
    public void setLocationTest() {
        testGeolocation.setLocation(mockLocation);
        assertEquals(mockLocation, testGeolocation.getLocation());
    }
}
