package com.ambulance;

import com.ambulance.model.EmergencyRequest;
import com.ambulance.model.EmergencyType;
import com.ambulance.model.Hospital;
import com.ambulance.model.Location;
import com.ambulance.model.Patient;
import com.ambulance.service.EmergencyService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmergencyServiceTest {

    private final EmergencyService emergencyService =
            new EmergencyService();

    private EmergencyRequest createEmergency(
            String id,
            EmergencyType type) {

        Patient patient =
                new Patient(
                        "P" + id,
                        "Patient " + id
                );

        Location pickup =
                new Location(
                        "Katpadi",
                        12.9692,
                        79.1559
                );

        Location hospitalLocation =
                new Location(
                        "Vellore",
                        12.9165,
                        79.1325
                );

        Hospital hospital =
                new Hospital(
                        "H001",
                        "CMC Hospital",
                        hospitalLocation
                );

        return new EmergencyRequest(
                id,
                patient,
                type,
                pickup,
                hospital
        );
    }

    @Test
    void testCriticalEmergencyHasHighestPriority() {

        EmergencyRequest normal =
                createEmergency(
                        "E001",
                        EmergencyType.NORMAL
                );

        EmergencyRequest critical =
                createEmergency(
                        "E002",
                        EmergencyType.CRITICAL
                );

        emergencyService.addEmergency(normal);
        emergencyService.addEmergency(critical);

        EmergencyRequest next =
                emergencyService.getNextEmergency();

        assertEquals(
                "E002",
                next.getEmergencyId()
        );

        assertEquals(
                EmergencyType.CRITICAL,
                next.getEmergencyType()
        );
    }

    @Test
    void testEmergencyPriorityOrder() {

        EmergencyRequest normal =
                createEmergency(
                        "E001",
                        EmergencyType.NORMAL
                );

        EmergencyRequest moderate =
                createEmergency(
                        "E002",
                        EmergencyType.MODERATE
                );

        EmergencyRequest high =
                createEmergency(
                        "E003",
                        EmergencyType.HIGH
                );

        EmergencyRequest critical =
                createEmergency(
                        "E004",
                        EmergencyType.CRITICAL
                );

        emergencyService.addEmergency(normal);
        emergencyService.addEmergency(moderate);
        emergencyService.addEmergency(high);
        emergencyService.addEmergency(critical);

        assertEquals(
                EmergencyType.CRITICAL,
                emergencyService
                        .getNextEmergency()
                        .getEmergencyType()
        );

        assertEquals(
                EmergencyType.HIGH,
                emergencyService
                        .getNextEmergency()
                        .getEmergencyType()
        );

        assertEquals(
                EmergencyType.MODERATE,
                emergencyService
                        .getNextEmergency()
                        .getEmergencyType()
        );

        assertEquals(
                EmergencyType.NORMAL,
                emergencyService
                        .getNextEmergency()
                        .getEmergencyType()
        );
    }

    @Test
    void testWaitingQueueCount() {

        EmergencyRequest emergency1 =
                createEmergency(
                        "E001",
                        EmergencyType.HIGH
                );

        EmergencyRequest emergency2 =
                createEmergency(
                        "E002",
                        EmergencyType.NORMAL
                );

        emergencyService.addEmergency(emergency1);
        emergencyService.addEmergency(emergency2);

        assertTrue(
                emergencyService.hasWaitingEmergencies()
        );

        assertEquals(
                2,
                emergencyService.getWaitingEmergencyCount()
        );
    }

    @Test
    void testEmergencyHistory() {

        EmergencyRequest emergency1 =
                createEmergency(
                        "E001",
                        EmergencyType.CRITICAL
                );

        EmergencyRequest emergency2 =
                createEmergency(
                        "E002",
                        EmergencyType.MODERATE
                );

        emergencyService.addEmergency(emergency1);
        emergencyService.addEmergency(emergency2);

        List<EmergencyRequest> history =
                emergencyService.getEmergencyHistory();

        assertEquals(2, history.size());

        assertTrue(
                history.contains(emergency1)
        );

        assertTrue(
                history.contains(emergency2)
        );
    }

    @Test
    void testRequeueEmergency() {

        EmergencyRequest emergency =
                createEmergency(
                        "E001",
                        EmergencyType.HIGH
                );

        emergencyService.requeueEmergency(emergency);

        assertTrue(
                emergencyService.hasWaitingEmergencies()
        );

        assertEquals(
                emergency,
                emergencyService.getNextEmergency()
        );
    }

    @Test
    void testNullEmergencyIsRejected() {

        assertThrows(
                RuntimeException.class,
                () -> emergencyService.addEmergency(null)
        );
    }
}