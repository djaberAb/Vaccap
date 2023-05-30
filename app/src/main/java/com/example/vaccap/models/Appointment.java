package com.example.vaccap.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Appointment {
    private String id;
    private String date;
    private String time;
    private String type;
    private String clinic;
    private String patient;
    private String status;

    public Appointment() {}

    public Appointment(String date, String time, String type, String clinic, String patient, String status) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.clinic = clinic;
        this.patient = patient;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> appointmentMap = new HashMap<>();
        appointmentMap.put("date", date);
        appointmentMap.put("time", time);
        appointmentMap.put("type", type);
        appointmentMap.put("clinic", clinic);
        appointmentMap.put("patient", patient);
        appointmentMap.put("status", status);
        return appointmentMap;
    }

    public static Appointment fromSnapshot(DocumentSnapshot snapshot) {
        Appointment appointment = snapshot.toObject(Appointment.class);
        appointment.setId(snapshot.getId());
        return appointment;
    }
}