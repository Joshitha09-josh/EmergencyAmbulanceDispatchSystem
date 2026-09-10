package com.ambulance.model;

public class Hospital {

    private String hospitalId;
    private String hospitalName;
    private Location location;

    public Hospital(
            String hospitalId,
            String hospitalName,
            Location location) {

        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.location = location;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalId='" + hospitalId + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", location=" + location +
                '}';
    }
}