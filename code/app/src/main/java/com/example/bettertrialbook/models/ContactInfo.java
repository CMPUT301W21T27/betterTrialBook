package com.example.bettertrialbook.models;

public class ContactInfo {
    private String email;
    private String phone;

    public ContactInfo() {
        email = " ";
        phone = " ";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
