package com.ambulance.model;

public class Ambulance {

    private String ambulanceId;
    private AmbulanceType ambulanceType;
    private AmbulanceStatus status;
    private Driver driver;
    private Location currentLocation;

    public Ambulance(
            String ambulanceId,
            AmbulanceType ambulanceType,
            Driver driver,
            Location currentLocation) {

        this.ambulanceId = ambulanceId;
        this.ambulanceType = ambulanceType;
        this.driver = driver;
        this.currentLocation = currentLocation;
        this.status = AmbulanceStatus.AVAILABLE;
    }

    public String getAmbulanceId() {
        return ambulanceId;
    }

    public AmbulanceType getAmbulanceType() {
        return ambulanceType;
    }

    public AmbulanceStatus getStatus() {
        return status;
    }

    public Driver getDriver() {
        return driver;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isAvailable() {
        return status == AmbulanceStatus.AVAILABLE;
    }

    public void setStatus(AmbulanceStatus status) {
        this.status = status;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public String toString() {
        return "Ambulance{" +
                "ambulanceId='" + ambulanceId + '\'' +
                ", ambulanceType=" + ambulanceType +
                ", status=" + status +
                ", driver=" + driver.getDriverName() +
                ", currentLocation=" + currentLocation +
                '}';
    }
}