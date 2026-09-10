package com.ambulance.service;

import com.ambulance.exception.AmbulanceAlreadyAssignedException;
import com.ambulance.exception.AmbulanceNotAvailableException;
import com.ambulance.model.Ambulance;
import com.ambulance.model.AmbulanceStatus;

import java.util.ArrayList;
import java.util.List;

public class AmbulanceService {

    private final List<Ambulance> ambulances =
            new ArrayList<>();

    public void addAmbulance(Ambulance ambulance) {

        if (ambulance == null) {
            throw new IllegalArgumentException(
                    "Ambulance cannot be null"
            );
        }

        ambulances.add(ambulance);
    }

    public List<Ambulance> getAmbulances() {
        return ambulances;
    }

    public List<Ambulance> getAvailableAmbulances() {

        return ambulances.stream()
                .filter(Ambulance::isAvailable)
                .toList();
    }

    public void dispatchAmbulance(
            Ambulance ambulance) {

        if (ambulance == null) {
            throw new AmbulanceNotAvailableException(
                    "Ambulance cannot be null"
            );
        }

        if (!ambulance.isAvailable()) {

            throw new AmbulanceAlreadyAssignedException(
                    "Ambulance "
                            + ambulance.getAmbulanceId()
                            + " is already assigned."
            );
        }

        ambulance.setStatus(
                AmbulanceStatus.DISPATCHED
        );
    }

    public void markEnRoute(
            Ambulance ambulance) {

        validateAmbulance(ambulance);

        ambulance.setStatus(
                AmbulanceStatus.EN_ROUTE
        );
    }

    public void markPatientPickedUp(
            Ambulance ambulance) {

        validateAmbulance(ambulance);

        ambulance.setStatus(
                AmbulanceStatus.PATIENT_PICKED_UP
        );
    }

    public void markHospitalArrived(
            Ambulance ambulance) {

        validateAmbulance(ambulance);

        ambulance.setStatus(
                AmbulanceStatus.HOSPITAL_ARRIVED
        );
    }

    public void makeAvailable(
            Ambulance ambulance) {

        validateAmbulance(ambulance);

        ambulance.setStatus(
                AmbulanceStatus.AVAILABLE
        );
    }

    private void validateAmbulance(
            Ambulance ambulance) {

        if (ambulance == null) {
            throw new AmbulanceNotAvailableException(
                    "Ambulance is not available."
            );
        }
    }
}