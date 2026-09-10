package com.ambulance;

import com.ambulance.model.Ambulance;
import com.ambulance.model.AmbulanceType;
import com.ambulance.model.Driver;
import com.ambulance.model.EmergencyRequest;
import com.ambulance.model.EmergencyType;
import com.ambulance.model.Hospital;
import com.ambulance.model.Location;
import com.ambulance.model.Patient;
import com.ambulance.service.AmbulanceService;
import com.ambulance.service.DispatchService;
import com.ambulance.service.DistanceService;
import com.ambulance.service.EmergencyService;

public class Main {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("   REAL-TIME EMERGENCY AMBULANCE SYSTEM");
        System.out.println("==============================================");

        // Create services
        AmbulanceService ambulanceService =
                new AmbulanceService();

        DistanceService distanceService =
                new DistanceService();

        EmergencyService emergencyService =
                new EmergencyService();

        DispatchService dispatchService =
                new DispatchService(
                        ambulanceService,
                        distanceService,
                        emergencyService
                );

        // Ambulance locations
        Location katpadi =
                new Location(
                        "Katpadi",
                        12.9692,
                        79.1559
                );

        Location vellore =
                new Location(
                        "Vellore",
                        12.9165,
                        79.1325
                );

        Location ranipet =
                new Location(
                        "Ranipet",
                        12.9276,
                        79.3330
                );

        // Drivers
        Driver driver1 =
                new Driver(
                        "D001",
                        "Ravi",
                        "9876543210",
                        "DL001"
                );

        Driver driver2 =
                new Driver(
                        "D002",
                        "Kumar",
                        "9876543211",
                        "DL002"
                );

        Driver driver3 =
                new Driver(
                        "D003",
                        "Arun",
                        "9876543212",
                        "DL003"
                );

        // Ambulances
        Ambulance ambulance1 =
                new Ambulance(
                        "AMB001",
                        AmbulanceType.BASIC,
                        driver1,
                        katpadi
                );

        Ambulance ambulance2 =
                new Ambulance(
                        "AMB002",
                        AmbulanceType.ADVANCED_LIFE_SUPPORT,
                        driver2,
                        vellore
                );

        Ambulance ambulance3 =
                new Ambulance(
                        "AMB003",
                        AmbulanceType.ICU,
                        driver3,
                        ranipet
                );

        ambulanceService.addAmbulance(ambulance1);
        ambulanceService.addAmbulance(ambulance2);
        ambulanceService.addAmbulance(ambulance3);

        // Display available ambulances
        System.out.println();
        System.out.println("AVAILABLE AMBULANCES");
        System.out.println("----------------------------------------------");

        for (Ambulance ambulance :
                ambulanceService.getAvailableAmbulances()) {

            System.out.println(
                    ambulance.getAmbulanceId()
                            + " | "
                            + ambulance.getAmbulanceType()
                            + " | Driver: "
                            + ambulance.getDriver().getDriverName()
            );
        }

        // Hospital
        Hospital hospital =
                new Hospital(
                        "H001",
                        "CMC Hospital",
                        vellore
                );

        // Patients
        Patient patient1 =
                new Patient(
                        "P001",
                        "Patient One"
                );

        Patient patient2 =
                new Patient(
                        "P002",
                        "Patient Two"
                );

        // Emergency requests
        EmergencyRequest criticalEmergency =
                new EmergencyRequest(
                        "E001",
                        patient1,
                        EmergencyType.CRITICAL,
                        katpadi,
                        hospital
                );

        EmergencyRequest normalEmergency =
                new EmergencyRequest(
                        "E002",
                        patient2,
                        EmergencyType.NORMAL,
                        katpadi,
                        hospital
                );

        // Add emergencies
        emergencyService.addEmergency(
                normalEmergency
        );

        emergencyService.addEmergency(
                criticalEmergency
        );

        System.out.println();
        System.out.println("EMERGENCY REQUESTS ADDED");
        System.out.println("----------------------------------------------");

        System.out.println("E001 -> CRITICAL");
        System.out.println("E002 -> NORMAL");

        // Dispatch highest priority emergency
        System.out.println();
        System.out.println("DISPATCHING EMERGENCY");
        System.out.println("----------------------------------------------");

        Ambulance selectedAmbulance =
                dispatchService.dispatchNextEmergency();

        if (selectedAmbulance != null) {

            EmergencyRequest assignedEmergency =
                    criticalEmergency;

            System.out.println(
                    "Emergency ID      : "
                            + assignedEmergency.getEmergencyId()
            );

            System.out.println(
                    "Emergency Type    : "
                            + assignedEmergency.getEmergencyType()
            );

            System.out.println(
                    "Ambulance ID      : "
                            + selectedAmbulance.getAmbulanceId()
            );

            System.out.println(
                    "Ambulance Type    : "
                            + selectedAmbulance.getAmbulanceType()
            );

            System.out.println(
                    "Driver            : "
                            + selectedAmbulance
                            .getDriver()
                            .getDriverName()
            );

            System.out.println(
                    "Distance          : "
                            + assignedEmergency
                            .getEstimatedDistance()
                            + " KM"
            );

            System.out.println(
                    "ETA               : "
                            + dispatchService
                            .getEstimatedArrivalMinutes(
                                    assignedEmergency
                            )
                            + " minutes"
            );

            System.out.println(
                    "Emergency Status  : "
                            + assignedEmergency.getStatus()
            );

            System.out.println(
                    "Ambulance Status  : "
                            + selectedAmbulance.getStatus()
            );
        }

        // Ambulance state transition
        System.out.println();
        System.out.println("AMBULANCE STATE TRANSITION");
        System.out.println("----------------------------------------------");

        dispatchService.markEnRoute(
                criticalEmergency
        );

        System.out.println(
                "1. En Route          -> "
                        + selectedAmbulance.getStatus()
        );

        dispatchService.markPatientPickedUp(
                criticalEmergency
        );

        System.out.println(
                "2. Patient Picked Up  -> "
                        + selectedAmbulance.getStatus()
        );

        dispatchService.markHospitalArrived(
                criticalEmergency
        );

        System.out.println(
                "3. Hospital Arrived   -> "
                        + selectedAmbulance.getStatus()
        );

        dispatchService.completeEmergency(
                criticalEmergency
        );

        System.out.println(
                "4. Available          -> "
                        + selectedAmbulance.getStatus()
        );

        // Waiting queue
        System.out.println();
        System.out.println("WAITING QUEUE");
        System.out.println("----------------------------------------------");

        System.out.println(
                "Waiting emergencies: "
                        + emergencyService
                        .getWaitingEmergencyCount()
        );

        Ambulance nextAmbulance =
                dispatchService.autoDispatchWaitingEmergency();

        if (nextAmbulance != null) {

            System.out.println(
                    "Waiting emergency automatically dispatched."
            );

            System.out.println(
                    "Ambulance: "
                            + nextAmbulance.getAmbulanceId()
            );
        }

        // Emergency history
        System.out.println();
        System.out.println("EMERGENCY HISTORY");
        System.out.println("----------------------------------------------");

        System.out.println(
                "Total emergencies recorded: "
                        + emergencyService
                        .getEmergencyHistory()
                        .size()
        );

        System.out.println();
        System.out.println("==============================================");
        System.out.println("        APPLICATION COMPLETED");
        System.out.println("==============================================");
    }
}