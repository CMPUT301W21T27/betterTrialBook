/*
Currently not implemented.
 */

package com.example.bettertrialbook.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Geolocation class stores a location object containing the lat and lon of the trial's geolocation.
 * Can be expanded to include more variables if necessary
 */
public class Geolocation implements Parcelable, Comparable<Geolocation> {

    Location location;

    /**
     * Creates a Geolocation
     * @param location
     *  the location of the Geolocation
     */
    public Geolocation(Location location) {
        this.location = location;
    }

    /**
     * gets the Geolocation's location
     * @return
     *  the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * sets the Geolocation's location
     * @param location
     *  the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    // parcelable methods
    protected Geolocation(Parcel in) {
        location = in.readParcelable(ContactInfo.class.getClassLoader());
    }

    public static final Creator<Geolocation> CREATOR = new Creator<Geolocation>() {
        @Override
        public Geolocation createFromParcel(Parcel in) {
            return new Geolocation(in);
        }

        @Override
        public Geolocation[] newArray(int size) {
            return new Geolocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeParcelable(location, i);
    }


    /**
     * This overrides the compareTo method from Comparable,
     * making it so that we can compare Geolocation objects
     * @param geolocation
     *  The geolocation being compared to
     * @return
     *  Returns 0 if the Geolocations are equal, non-zero otherwise
     */
    @Override
    public int compareTo(Geolocation geolocation) {
        return String.valueOf(this.location.getLatitude()).compareTo(String.valueOf(geolocation.getLocation().getLatitude()))
                + String.valueOf(this.location.getLongitude()).compareTo(String.valueOf(geolocation.getLocation().getLongitude()));
    }
}
