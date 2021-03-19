package com.example.bettertrialbook.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * User object
 * Represents the app's Users with a username, ID, and contact info
 */
public class User implements Parcelable {
    private String ID;
    private String username;
    private ContactInfo contact;

    /**
     * Constructs a User object with an ID, and other fields as empty strings
     * @param ID - Creates user object
     */
    public User(String ID) {
        this.ID = ID;
        username="";
        contact = new ContactInfo();
    }

    /**
     * Returns the ID of the user object
     * @return - returns ID of user
     */
    public String getID() {
        return ID;
    }

    /**
     * Retruns the username of the user object
     * @return - returns Username of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username - Sets username of user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param email - Sets email of contact info
     * @param phone - Sets phone of contact info
     */
    public void setContact(String email, String phone) {
        contact.setEmail(email);
        contact.setPhone(phone);
    }

    /**
     * Returns contactInfo object related to user object
     * @return - Returns contact info
     */
    public ContactInfo getContact() {
        return contact;
    }


    @Override
    public boolean equals(Object obj){
        if(obj==null){
            return false;
        }

        if(this.getClass()==obj.getClass()){
            User user = (User) obj;
            return this.ID.equals(user.getID());

        }else{
            return false;
        }
    }

    //Parcelable methods
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
