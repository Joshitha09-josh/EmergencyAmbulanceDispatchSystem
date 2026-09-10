package com.ambulance.service;

import com.ambulance.model.Location;

public class DistanceService {

    public double calculateDistance(
            Location first,
            Location second) {

        if (first == null || second == null) {
            throw new IllegalArgumentException(
                    "Locations cannot be null"
            );
        }

        double latitudeDifference =
                first.getLatitude() - second.getLatitude();

        double longitudeDifference =
                first.getLongitude() - second.getLongitude();

        double distance =
                Math.sqrt(
                        Math.pow(latitudeDifference, 2)
                                + Math.pow(longitudeDifference, 2)
                ) * 111;

        return Math.round(distance * 100.0) / 100.0;
    }

    public int calculateEstimatedArrivalMinutes(
            double distanceKm) {

        if (distanceKm < 0) {
            throw new IllegalArgumentException(
                    "Distance cannot be negative"
            );
        }

        double averageSpeedKmPerHour = 40.0;

        double timeInHours =
                distanceKm / averageSpeedKmPerHour;

        return (int) Math.ceil(
                timeInHours * 60
        );
    }
}