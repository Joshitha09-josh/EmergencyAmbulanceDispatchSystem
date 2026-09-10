package com.ambulance.model;

import java.time.LocalDateTime;

public class EmergencyRequest {

    private String emergencyId;
    private Patient patient;
    private EmergencyType emergencyType;
    private Location pickupLocation;
    private Hospital destinationHospital;

    private EmergencyStatus status;

    private Ambulance assignedAmbulance;

    private double estimatedDistance;

    private LocalDateTime requestTime;
    private LocalDateTime estimatedArrivalTime;

    public EmergencyRequest(
            String emergencyId,
            Patient patient,
            EmergencyType emergencyType,
            Location pickupLocation,
            Hospital destinationHospital) {

        this.emergencyId = emergencyId;
        this.patient = patient;
        this.emergencyType = emergencyType;
        this.pickupLocation = pickupLocation;
        this.destinationHospital = destinationHospital;

        this.status = EmergencyStatus.WAITING;
        this.requestTime = LocalDateTime.now();
    }

    public String getEmergencyId() {
        return emergencyId;
    }

    public Patient getPatient() {
        return patient;
    }

    public EmergencyType getEmergencyType() {
        return emergencyType;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Hospital getDestinationHospital() {
        return destinationHospital;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public Ambulance getAssignedAmbulance() {
        return assignedAmbulance;
    }

    public double getEstimatedDistance() {
        return estimatedDistance;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public LocalDateTime getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public void setAssignedAmbulance(Ambulance assignedAmbulance) {
        this.assignedAmbulance = assignedAmbulance;
    }

    public void setEstimatedDistance(double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }

    public void setEstimatedArrivalTime(
            LocalDateTime estimatedArrivalTime) {

        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    @Override
    public String toString() {
        return "EmergencyRequest{" +
                "emergencyId='" + emergencyId + '\'' +
                ", patient=" + patient.getPatientName() +
                ", emergencyType=" + emergencyType +
                ", status=" + status +
                ", assignedAmbulance=" +
                (assignedAmbulance != null
                        ? assignedAmbulance.getAmbulanceId()
                        : "None") +
                ", estimatedDistance=" +
                estimatedDistance +
                '}';
    }
}