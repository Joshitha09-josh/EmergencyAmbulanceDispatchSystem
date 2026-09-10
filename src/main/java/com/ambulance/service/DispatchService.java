package com.ambulance.service;

import com.ambulance.exception.AmbulanceNotAvailableException;
import com.ambulance.model.Ambulance;
import com.ambulance.model.AmbulanceType;
import com.ambulance.model.EmergencyRequest;
import com.ambulance.model.EmergencyStatus;
import com.ambulance.model.EmergencyType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class DispatchService {

    private final AmbulanceService ambulanceService;
    private final DistanceService distanceService;
    private final EmergencyService emergencyService;

    public DispatchService(
            AmbulanceService ambulanceService,
            DistanceService distanceService,
            EmergencyService emergencyService) {

        this.ambulanceService = ambulanceService;
        this.distanceService = distanceService;
        this.emergencyService = emergencyService;
    }

    public Ambulance dispatchNextEmergency() {

        EmergencyRequest emergency =
                emergencyService.getNextEmergency();

        if (emergency == null) {
            return null;
        }

        try {
            return dispatchEmergency(emergency);
        } catch (AmbulanceNotAvailableException e) {
            returnEmergencyToWaitingQueue(emergency);
            return null;
        }
    }

    public Ambulance dispatchEmergency(
            EmergencyRequest emergency) {

        if (emergency == null) {
            throw new IllegalArgumentException(
                    "Emergency cannot be null."
            );
        }

        List<Ambulance> availableAmbulances =
                ambulanceService.getAvailableAmbulances();

        if (availableAmbulances.isEmpty()) {

            emergency.setStatus(EmergencyStatus.WAITING);

            throw new AmbulanceNotAvailableException(
                    "No ambulance is currently available."
            );
        }

        Ambulance selectedAmbulance =
                findBestAmbulance(
                        emergency,
                        availableAmbulances
                );

        if (selectedAmbulance == null) {

            emergency.setStatus(EmergencyStatus.WAITING);

            throw new AmbulanceNotAvailableException(
                    "No suitable ambulance is available."
            );
        }

        double distance =
                distanceService.calculateDistance(
                        selectedAmbulance.getCurrentLocation(),
                        emergency.getPickupLocation()
                );

        int etaMinutes =
                distanceService
                        .calculateEstimatedArrivalMinutes(distance);

        ambulanceService.dispatchAmbulance(
                selectedAmbulance
        );

        emergency.setAssignedAmbulance(
                selectedAmbulance
        );

        emergency.setEstimatedDistance(distance);

        emergency.setEstimatedArrivalTime(
                LocalDateTime.now()
                        .plusMinutes(etaMinutes)
        );

        emergency.setStatus(
                EmergencyStatus.DISPATCHED
        );

        return selectedAmbulance;
    }

    private Ambulance findBestAmbulance(
            EmergencyRequest emergency,
            List<Ambulance> availableAmbulances) {

        EmergencyType emergencyType =
                emergency.getEmergencyType();

        return availableAmbulances.stream()

                .filter(ambulance ->
                        isSuitableType(
                                ambulance.getAmbulanceType(),
                                emergencyType
                        )
                )

                .min(
                        Comparator.comparingDouble(
                                ambulance ->
                                        distanceService.calculateDistance(
                                                ambulance.getCurrentLocation(),
                                                emergency.getPickupLocation()
                                        )
                        )
                )

                .orElse(null);
    }

    private boolean isSuitableType(
            AmbulanceType ambulanceType,
            EmergencyType emergencyType) {

        switch (emergencyType) {

            case CRITICAL:
                return ambulanceType ==
                        AmbulanceType.ICU
                        || ambulanceType ==
                        AmbulanceType.ADVANCED_LIFE_SUPPORT;

            case HIGH:
                return ambulanceType ==
                        AmbulanceType.ICU
                        || ambulanceType ==
                        AmbulanceType.ADVANCED_LIFE_SUPPORT;

            case MODERATE:
                return ambulanceType ==
                        AmbulanceType.ADVANCED_LIFE_SUPPORT
                        || ambulanceType ==
                        AmbulanceType.BASIC;

            case NORMAL:
                return true;

            default:
                return false;
        }
    }

    public void markEnRoute(
            EmergencyRequest emergency) {

        validateAssignedEmergency(emergency);

        ambulanceService.markEnRoute(
                emergency.getAssignedAmbulance()
        );

        emergency.setStatus(
                EmergencyStatus.EN_ROUTE
        );
    }

    public void markPatientPickedUp(
            EmergencyRequest emergency) {

        validateAssignedEmergency(emergency);

        ambulanceService.markPatientPickedUp(
                emergency.getAssignedAmbulance()
        );

        emergency.setStatus(
                EmergencyStatus.PATIENT_PICKED_UP
        );
    }

    public void markHospitalArrived(
            EmergencyRequest emergency) {

        validateAssignedEmergency(emergency);

        ambulanceService.markHospitalArrived(
                emergency.getAssignedAmbulance()
        );

        emergency.setStatus(
                EmergencyStatus.HOSPITAL_ARRIVED
        );
    }

    public void completeEmergency(
            EmergencyRequest emergency) {

        validateAssignedEmergency(emergency);

        Ambulance ambulance =
                emergency.getAssignedAmbulance();

        ambulanceService.makeAvailable(ambulance);

        emergency.setStatus(
                EmergencyStatus.COMPLETED
        );

        /*
         * When the ambulance becomes available,
         * automatically try the next waiting emergency.
         */
        autoDispatchWaitingEmergency();
    }

    public Ambulance autoDispatchWaitingEmergency() {

        if (!emergencyService.hasWaitingEmergencies()) {
            return null;
        }

        return dispatchNextEmergency();
    }

    private void returnEmergencyToWaitingQueue(
            EmergencyRequest emergency) {

        emergency.setStatus(EmergencyStatus.WAITING);

        emergencyService.requeueEmergency(emergency);
    }

    private void validateAssignedEmergency(
            EmergencyRequest emergency) {

        if (emergency == null) {
            throw new IllegalArgumentException(
                    "Emergency cannot be null."
            );
        }

        if (emergency.getAssignedAmbulance() == null) {
            throw new AmbulanceNotAvailableException(
                    "No ambulance is assigned to this emergency."
            );
        }
    }

    public int getEstimatedArrivalMinutes(
            EmergencyRequest emergency) {

        if (emergency == null) {
            throw new IllegalArgumentException(
                    "Emergency cannot be null."
            );
        }

        if (emergency.getEstimatedArrivalTime() == null) {
            return 0;
        }

        return (int) Math.ceil(
                java.time.Duration.between(
                        LocalDateTime.now(),
                        emergency.getEstimatedArrivalTime()
                ).toSeconds() / 60.0
        );
    }
}