package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int ID;
    private String username;
    private ContactInfo contact;

    public User(int ID) {
        this.ID = ID;
        username=" ";
        contact = new ContactInfo();
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContact(String email, String phone) {
        contact.setEmail(email);
        contact.setPhone(phone);
    }

    public ContactInfo getContact(ContactInfo contact) {
        return contact;
    }


    //Parcelable methods to send user between activities
    protected User(Parcel in) {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
