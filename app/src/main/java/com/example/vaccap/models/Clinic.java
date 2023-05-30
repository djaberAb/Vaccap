package com.example.vaccap.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Clinic {

    private String email;
    public String clinicName;
    public String phoneNumber;

    public Clinic() {
    }

    public Clinic(String email, String clinicName, String phoneNumber) {
        this.email = email;
        this.clinicName = clinicName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters for each field
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("clinicName", clinicName);
        result.put("phoneNumber", phoneNumber);
        return result;
    }

    public static Clinic fromSnapshot(DocumentSnapshot snapshot) {
        Clinic clinic = new Clinic();
        // Get the document data as a map
        Map<String, Object> clinicData = snapshot.getData();
        clinic.setEmail((String) clinicData.get("email"));
        clinic.setClinicName((String) clinicData.get("clinicName"));
        clinic.setPhoneNumber((String) clinicData.get("phoneNumber"));

        return clinic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
