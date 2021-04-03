/*
Currently not implemented.
 */

package com.example.bettertrialbook.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Geolocation implements Parcelable {

    Location location;

    public Geolocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

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
}
