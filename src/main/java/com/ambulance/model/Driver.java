package com.ambulance.model;

public class Driver {

    private String driverId;
    private String driverName;
    private String phoneNumber;
    private String licenseNumber;

    public Driver(
            String driverId,
            String driverName,
            String phoneNumber,
            String licenseNumber) {

        this.driverId = driverId;
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                '}';
    }
}