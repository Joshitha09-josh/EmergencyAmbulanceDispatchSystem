package com.ambulance.model;

public class Patient {

    private String patientId;
    private String patientName;

    public Patient(String patientId, String patientName) {
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                '}';
    }
}