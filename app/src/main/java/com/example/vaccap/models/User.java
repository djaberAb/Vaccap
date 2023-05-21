package com.example.vaccap.models;


import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String email;
    public String userName;
    public String fullName;
    public String phoneNumber;
    public String nameBaby;
    public String bDayBaby;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNameBaby() {
        return nameBaby;
    }

    public void setNameBaby(String nameBaby) {
        this.nameBaby = nameBaby;
    }

    public String getbDayBaby() {
        return bDayBaby;
    }

    public void setbDayBaby(String bDayBaby) {
        this.bDayBaby = bDayBaby;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {

    }

    public User(String email,String userNameUser, String fullNameUser, String phoneNumberUser, String nameBaby, String bDayBaby) {
        this.email = email;
        this.userName = userNameUser;
        this.fullName = fullNameUser;
        this.phoneNumber = phoneNumberUser;
        this.nameBaby = nameBaby;
        this.bDayBaby = bDayBaby;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("userName", userName);
        result.put("fullName", fullName);
        result.put("phoneNumber", phoneNumber);
        result.put("nameBaby", nameBaby);
        result.put("bDayBaby", bDayBaby);
        return result;

    }
}
