package com.example.vaccap.models;

public class VaccineInfo {
    private String ageRange;
    private String vaccineNames;

    private Boolean is_available;

    public VaccineInfo(String ageRange, String vaccineNames, Boolean is_available) {
        this.ageRange = ageRange;
        this.vaccineNames = vaccineNames;
        this.is_available = is_available;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getVaccineNames() {
        return vaccineNames;
    }

    public void setVaccineNames(String vaccineNames) {
        this.vaccineNames = vaccineNames;
    }

    public Boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }
}
