package com.ambulance.service;

import com.ambulance.exception.InvalidEmergencyException;
import com.ambulance.model.EmergencyRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class EmergencyService {

    private final PriorityQueue<EmergencyRequest> waitingQueue =
            new PriorityQueue<>(
                    (first, second) ->
                            Integer.compare(
                                    first.getEmergencyType()
                                            .getPriority(),
                                    second.getEmergencyType()
                                            .getPriority()
                            )
            );

    private final List<EmergencyRequest> emergencyHistory =
            new ArrayList<>();

    public void addEmergency(
            EmergencyRequest request) {

        validateEmergency(request);

        waitingQueue.offer(request);

        emergencyHistory.add(request);
    }

    public EmergencyRequest getNextEmergency() {

        return waitingQueue.poll();
    }

   public void requeueEmergency(EmergencyRequest request) {

    if (request == null) {
        throw new InvalidEmergencyException(
                "Emergency request cannot be null."
        );
    }

    waitingQueue.offer(request);
}
    public boolean hasWaitingEmergencies() {

        return !waitingQueue.isEmpty();
    }

    public int getWaitingEmergencyCount() {

        return waitingQueue.size();
    }

    public List<EmergencyRequest>
    getEmergencyHistory() {

        return emergencyHistory;
    }

    private void validateEmergency(
            EmergencyRequest request) {

        if (request == null) {
            throw new InvalidEmergencyException(
                    "Emergency request cannot be null."
            );
        }

        if (request.getEmergencyId() == null
                || request.getEmergencyId().isBlank()) {

            throw new InvalidEmergencyException(
                    "Emergency ID is required."
            );
        }

        if (request.getPatient() == null) {

            throw new InvalidEmergencyException(
                    "Patient information is required."
            );
        }

        if (request.getEmergencyType() == null) {

            throw new InvalidEmergencyException(
                    "Emergency type is required."
            );
        }

        if (request.getPickupLocation() == null) {

            throw new InvalidEmergencyException(
                    "Pickup location is required."
            );
        }

        if (request.getDestinationHospital() == null) {

            throw new InvalidEmergencyException(
                    "Destination hospital is required."
            );
        }
    }
}