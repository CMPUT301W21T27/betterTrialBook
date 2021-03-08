package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String ID;
    private String username;
    private ContactInfo contact;

    public User(String ID) {
        this.ID = ID;
        username="";
        contact = new ContactInfo();
    }

    protected User(Parcel in) {
        ID = in.readString();
        username = in.readString();
        contact = in.readParcelable(ContactInfo.class.getClassLoader());
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

    public String getID() {
        return ID;
    }

    public void setID(String id){
        this.ID=id;
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

    public ContactInfo getContact() {
        return contact;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(username);
        dest.writeParcelable(contact, flags);
    }
}
