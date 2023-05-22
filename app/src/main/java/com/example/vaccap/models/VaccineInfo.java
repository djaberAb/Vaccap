package com.example.vaccap.models;

public class VaccineInfo {
    private String ageRange;
    private String vaccineNames;

    public VaccineInfo(String ageRange, String vaccineNames) {
        this.ageRange = ageRange;
        this.vaccineNames = vaccineNames;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getVaccineNames() {
        return vaccineNames;
    }
}
