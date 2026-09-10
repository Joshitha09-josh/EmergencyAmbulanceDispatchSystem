package com.ambulance;

import com.ambulance.model.Location;
import com.ambulance.service.DistanceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceServiceTest {

    private final DistanceService distanceService =
            new DistanceService();

    @Test
    void testCalculateDistance() {

        Location location1 =
                new Location(
                        "Katpadi",
                        12.9692,
                        79.1559
                );

        Location location2 =
                new Location(
                        "Vellore",
                        12.9165,
                        79.1325
                );

        double distance =
                distanceService.calculateDistance(
                        location1,
                        location2
                );

        assertTrue(distance > 0);

        System.out.println(
                "Calculated Distance: "
                        + distance
                        + " KM"
        );
    }

    @Test
    void testEstimatedArrivalTime() {

        double distance = 20.0;

        int eta =
                distanceService
                        .calculateEstimatedArrivalMinutes(
                                distance
                        );

        assertEquals(30, eta);

        System.out.println(
                "Estimated Arrival Time: "
                        + eta
                        + " minutes"
        );
    }

    @Test
    void testZeroDistance() {

        Location location =
                new Location(
                        "Hospital",
                        12.9165,
                        79.1325
                );

        double distance =
                distanceService.calculateDistance(
                        location,
                        location
                );

        assertEquals(0.0, distance);
    }

    @Test
    void testNegativeDistanceThrowsException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> distanceService
                        .calculateEstimatedArrivalMinutes(-10)
        );
    }

    @Test
    void testNullLocationThrowsException() {

        Location location =
                new Location(
                        "Hospital",
                        12.9165,
                        79.1325
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> distanceService.calculateDistance(
                        null,
                        location
                )
        );
    }
}