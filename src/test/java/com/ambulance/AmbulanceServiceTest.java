package com.ambulance;

import com.ambulance.exception.AmbulanceAlreadyAssignedException;
import com.ambulance.model.Ambulance;
import com.ambulance.model.AmbulanceStatus;
import com.ambulance.model.AmbulanceType;
import com.ambulance.model.Driver;
import com.ambulance.model.Location;
import com.ambulance.service.AmbulanceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AmbulanceServiceTest {

    private final AmbulanceService ambulanceService =
            new AmbulanceService();

    private Ambulance createAmbulance() {

        Driver driver =
                new Driver(
                        "D001",
                        "Ravi",
                        "9876543210",
                        "DL12345"
                );

        Location location =
                new Location(
                        "Katpadi",
                        12.9692,
                        79.1559
                );

        return new Ambulance(
                "AMB001",
                AmbulanceType.BASIC,
                driver,
                location
        );
    }

    @Test
    void testAddAmbulance() {

        Ambulance ambulance = createAmbulance();

        ambulanceService.addAmbulance(ambulance);

        assertEquals(
                1,
                ambulanceService.getAmbulances().size()
        );
    }

    @Test
    void testAmbulanceInitiallyAvailable() {

        Ambulance ambulance = createAmbulance();

        assertEquals(
                AmbulanceStatus.AVAILABLE,
                ambulance.getStatus()
        );

        assertTrue(ambulance.isAvailable());
    }

    @Test
    void testDispatchAmbulance() {

        Ambulance ambulance = createAmbulance();

        ambulanceService.addAmbulance(ambulance);

        ambulanceService.dispatchAmbulance(ambulance);

        assertEquals(
                AmbulanceStatus.DISPATCHED,
                ambulance.getStatus()
        );

        assertFalse(ambulance.isAvailable());
    }

    @Test
    void testAmbulanceStateFlow() {

        Ambulance ambulance = createAmbulance();

        ambulanceService.dispatchAmbulance(ambulance);

        assertEquals(
                AmbulanceStatus.DISPATCHED,
                ambulance.getStatus()
        );

        ambulanceService.markEnRoute(ambulance);

        assertEquals(
                AmbulanceStatus.EN_ROUTE,
                ambulance.getStatus()
        );

        ambulanceService.markPatientPickedUp(ambulance);

        assertEquals(
                AmbulanceStatus.PATIENT_PICKED_UP,
                ambulance.getStatus()
        );

        ambulanceService.markHospitalArrived(ambulance);

        assertEquals(
                AmbulanceStatus.HOSPITAL_ARRIVED,
                ambulance.getStatus()
        );

        ambulanceService.makeAvailable(ambulance);

        assertEquals(
                AmbulanceStatus.AVAILABLE,
                ambulance.getStatus()
        );

        assertTrue(ambulance.isAvailable());
    }

    @Test
    void testCannotDispatchBusyAmbulance() {

        Ambulance ambulance = createAmbulance();

        ambulanceService.dispatchAmbulance(ambulance);

        assertThrows(
                AmbulanceAlreadyAssignedException.class,
                () -> ambulanceService
                        .dispatchAmbulance(ambulance)
        );
    }

    @Test
    void testGetAvailableAmbulances() {

        Ambulance ambulance1 = createAmbulance();

        Driver driver2 =
                new Driver(
                        "D002",
                        "Kumar",
                        "9876500000",
                        "DL67890"
                );

        Location location2 =
                new Location(
                        "Vellore",
                        12.9165,
                        79.1325
                );

        Ambulance ambulance2 =
                new Ambulance(
                        "AMB002",
                        AmbulanceType.ADVANCED_LIFE_SUPPORT,
                        driver2,
                        location2
                );

        ambulanceService.addAmbulance(ambulance1);
        ambulanceService.addAmbulance(ambulance2);

        assertEquals(
                2,
                ambulanceService
                        .getAvailableAmbulances()
                        .size()
        );

        ambulanceService.dispatchAmbulance(ambulance1);

        assertEquals(
                1,
                ambulanceService
                        .getAvailableAmbulances()
                        .size()
        );

        assertEquals(
                "AMB002",
                ambulanceService
                        .getAvailableAmbulances()
                        .get(0)
                        .getAmbulanceId()
        );
    }
}