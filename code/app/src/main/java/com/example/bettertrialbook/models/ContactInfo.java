package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

/*
* The contact info of User objects
* */
public class ContactInfo implements Parcelable {
    private String email;
    private String phone;

    /**
     * Constructs ContactInfo object with fields as empty strings
     */
    public ContactInfo() {
        email = "";
        phone = "";
    }

    /**
     * Returns users email address
     * @return - returns email string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets users email address
     * @param email - sets email to input
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns user's phone number
     * @return - returns phone string
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets user's phone number
     * @param phone - sets phone to input
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Parcelable methods
    protected ContactInfo(Parcel in) {
        email = in.readString();
        phone = in.readString();
    }

    public static final Creator<ContactInfo> CREATOR = new Creator<ContactInfo>() {
        @Override
        public ContactInfo createFromParcel(Parcel in) {
            return new ContactInfo(in);
        }

        @Override
        public ContactInfo[] newArray(int size) {
            return new ContactInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(phone);
    }
}
