package com.ambulance;

import com.ambulance.exception.AmbulanceNotAvailableException;
import com.ambulance.model.Ambulance;
import com.ambulance.model.AmbulanceStatus;
import com.ambulance.model.AmbulanceType;
import com.ambulance.model.Driver;
import com.ambulance.model.EmergencyRequest;
import com.ambulance.model.EmergencyStatus;
import com.ambulance.model.EmergencyType;
import com.ambulance.model.Hospital;
import com.ambulance.model.Location;
import com.ambulance.model.Patient;
import com.ambulance.service.AmbulanceService;
import com.ambulance.service.DispatchService;
import com.ambulance.service.DistanceService;
import com.ambulance.service.EmergencyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DispatchServiceTest {

    private AmbulanceService ambulanceService;
    private DistanceService distanceService;
    private EmergencyService emergencyService;
    private DispatchService dispatchService;

    private void setup() {

        ambulanceService = new AmbulanceService();
        distanceService = new DistanceService();
        emergencyService = new EmergencyService();

        dispatchService = new DispatchService(
                ambulanceService,
                distanceService,
                emergencyService
        );
    }

    private EmergencyRequest createEmergency(
            String id,
            EmergencyType type) {

        Patient patient = new Patient(
                "P" + id,
                "Patient " + id
        );

        Location pickup = new Location(
                "Katpadi",
                12.9692,
                79.1559
        );

        Location hospitalLocation = new Location(
                "Vellore",
                12.9165,
                79.1325
        );

        Hospital hospital = new Hospital(
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

    private Ambulance createAmbulance(
            String id,
            AmbulanceType type,
            String locationName,
            double latitude,
            double longitude) {

        Driver driver = new Driver(
                "D" + id,
                "Driver " + id,
                "9876543210",
                "DL" + id
        );

        Location location = new Location(
                locationName,
                latitude,
                longitude
        );

        return new Ambulance(
                id,
                type,
                driver,
                location
        );
    }

    // Test 1
    @Test
    void testCriticalEmergencyGetsSuitableAmbulance() {

        setup();

        Ambulance basic = createAmbulance(
                "AMB001",
                AmbulanceType.BASIC,
                "Katpadi",
                12.9692,
                79.1559
        );

        Ambulance advanced = createAmbulance(
                "AMB002",
                AmbulanceType.ADVANCED_LIFE_SUPPORT,
                "Vellore",
                12.9165,
                79.1325
        );

        ambulanceService.addAmbulance(basic);
        ambulanceService.addAmbulance(advanced);

        EmergencyRequest emergency =
                createEmergency(
                        "E001",
                        EmergencyType.CRITICAL
                );

        Ambulance selected =
                dispatchService.dispatchEmergency(
                        emergency
                );

        assertNotNull(selected);

        assertEquals(
                AmbulanceType.ADVANCED_LIFE_SUPPORT,
                selected.getAmbulanceType()
        );

        assertEquals(
                "AMB002",
                selected.getAmbulanceId()
        );
    }

    // Test 2
    @Test
    void testNearestSuitableAmbulanceIsSelected() {

        setup();

        Ambulance farAmbulance = createAmbulance(
                "AMB001",
                AmbulanceType.BASIC,
                "Chennai",
                13.0827,
                80.2707
        );

        Ambulance nearAmbulance = createAmbulance(
                "AMB002",
                AmbulanceType.BASIC,
                "Katpadi",
                12.9700,
                79.1560
        );

        ambulanceService.addAmbulance(farAmbulance);
        ambulanceService.addAmbulance(nearAmbulance);

        EmergencyRequest emergency =
                createEmergency(
                        "E002",
                        EmergencyType.NORMAL
                );

        Ambulance selected =
                dispatchService.dispatchEmergency(
                        emergency
                );

        assertNotNull(selected);

        assertEquals(
                "AMB002",
                selected.getAmbulanceId()
        );
    }

    // Test 3
    @Test
    void testAmbulanceCannotBeAssignedTwice() {

        setup();

        Ambulance ambulance = createAmbulance(
                "AMB001",
                AmbulanceType.BASIC,
                "Katpadi",
                12.9692,
                79.1559
        );

        ambulanceService.addAmbulance(ambulance);

        EmergencyRequest emergency1 =
                createEmergency(
                        "E001",
                        EmergencyType.NORMAL
                );

        EmergencyRequest emergency2 =
                createEmergency(
                        "E002",
                        EmergencyType.NORMAL
                );

        // First emergency gets the ambulance
        dispatchService.dispatchEmergency(emergency1);

        assertEquals(
                AmbulanceStatus.DISPATCHED,
                ambulance.getStatus()
        );

        // Second emergency must not get the same ambulance
        assertThrows(
                AmbulanceNotAvailableException.class,
                () -> dispatchService.dispatchEmergency(
                        emergency2
                )
        );
    }

    // Test 4
    @Test
    void testEmergencyGetsDispatched() {

        setup();

        Ambulance ambulance = createAmbulance(
                "AMB001",
                AmbulanceType.BASIC,
                "Katpadi",
                12.9692,
                79.1559
        );

        ambulanceService.addAmbulance(ambulance);

        EmergencyRequest emergency =
                createEmergency(
                        "E003",
                        EmergencyType.NORMAL
                );

        Ambulance selected =
                dispatchService.dispatchEmergency(
                        emergency
                );

        assertNotNull(selected);

        assertEquals(
                EmergencyStatus.DISPATCHED,
                emergency.getStatus()
        );

        assertNotNull(
                emergency.getAssignedAmbulance()
        );

        assertNotNull(
                emergency.getEstimatedArrivalTime()
        );

        assertTrue(
                emergency.getEstimatedDistance() >= 0
        );
    }

    // Test 5
    @Test
    void testAmbulanceStatusFlow() {

        setup();

        Ambulance ambulance = createAmbulance(
                "AMB001",
                AmbulanceType.BASIC,
                "Katpadi",
                12.9692,
                79.1559
        );

        ambulanceService.addAmbulance(ambulance);

        EmergencyRequest emergency =
                createEmergency(
                        "E004",
                        EmergencyType.NORMAL
                );

        dispatchService.dispatchEmergency(emergency);

        assertEquals(
                EmergencyStatus.DISPATCHED,
                emergency.getStatus()
        );

        dispatchService.markEnRoute(emergency);

        assertEquals(
                EmergencyStatus.EN_ROUTE,
                emergency.getStatus()
        );

        dispatchService.markPatientPickedUp(emergency);

        assertEquals(
                EmergencyStatus.PATIENT_PICKED_UP,
                emergency.getStatus()
        );

        dispatchService.markHospitalArrived(emergency);

        assertEquals(
                EmergencyStatus.HOSPITAL_ARRIVED,
                emergency.getStatus()
        );

        dispatchService.completeEmergency(emergency);

        assertEquals(
                EmergencyStatus.COMPLETED,
                emergency.getStatus()
        );

        assertEquals(
                AmbulanceStatus.AVAILABLE,
                ambulance.getStatus()
        );
    }
}