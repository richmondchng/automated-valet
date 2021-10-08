package org.richmondchng.automatedvalet.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.richmondchng.automatedvalet.data.ParkingGarage;
import org.richmondchng.automatedvalet.exception.VehicleAlreadyParkedException;
import org.richmondchng.automatedvalet.model.parking.ParkingLot;
import org.richmondchng.automatedvalet.model.vehicle.Vehicle;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test ParkingValetService.
 *
 * @author richmondchng
 */
@ExtendWith(MockitoExtension.class)
class ParkingValetServiceTest {

    @Mock
    private ParkingGarage parkingGarage;

    // test instance
    private ParkingValetService parkingValetService;

    @BeforeEach
    void setUp() {
        parkingValetService = new ParkingValetService(parkingGarage);
    }

    @AfterEach
    void tearDown() {
        parkingValetService = null;
    }

    /**
     * Test parkVehicle. Parameter is null, throw exception.
     */
    @Test
    void parkVehicle_nullParameterVehicleType_throwException() {
        try {
            parkingValetService.parkVehicle(null, "YEE4562U", LocalDateTime.now());
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("Vehicle type is required", e.getMessage());
            verifyNoInteractions(parkingGarage);
        }
    }

    /**
     * Test parkVehicle. Parameter is null, throw exception.
     */
    @Test
    void parkVehicle_nullParameterLicensePlate_throwException() {
        try {
            parkingValetService.parkVehicle(VehicleType.CAR, null, LocalDateTime.now());
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("License plate is required", e.getMessage());
            verifyNoInteractions(parkingGarage);
        }
    }

    /**
     * Test parkVehicle. Parameter is null, throw exception.
     */
    @Test
    void parkVehicle_nullParameterTimeIn_throwException() {
        try {
            parkingValetService.parkVehicle(VehicleType.CAR, "YEE4562U", null);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("Time in is required", e.getMessage());
            verifyNoInteractions(parkingGarage);
        }
    }

    /**
     * Test parkVehicle. No available parking lots, return null.
     */
    @Test
    void parkVehicle_noAvailableParking_returnNull() {
        when(parkingGarage.getByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot1").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "ABC1234X")).build(),
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot2").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "DBC2234X")).build(),
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot3").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "DBC1234X")).build(),
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot4").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "ABC2234X")).build()
        ));

        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 7, 10, 40, 23);
        final ParkingLot result = parkingValetService.parkVehicle(VehicleType.CAR, "YEE4562U", timeIn);

        verify(parkingGarage, times(1)).getByVehicleType(VehicleType.CAR);

        assertNull(result);
    }

    /**
     * Test parkVehicle. Has available parking lots, return parking lot details.
     */
    @Test
    void parkVehicle_hasAvailableParking_returnParkingLot() {
        when(parkingGarage.getByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot1").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "ABC1234X")).build(),
                // empty lot
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot2").build(),
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot3").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "DBC1234X")).build(),
                // empty lot
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot4").build()
        ));

        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 7, 10, 40, 23);
        final ParkingLot result = parkingValetService.parkVehicle(VehicleType.CAR, "YEE4562U", timeIn);

        verify(parkingGarage, times(1)).getByVehicleType(VehicleType.CAR);

        assertNotNull(result);
        assertEquals("CarLot2", result.getLabel());
        assertEquals(timeIn, result.getTimeIn());
    }

    /**
     * Test parkVehicle. Has available parking lot, but vehicle is already parked.
     */
    @Test
    void parkVehicle_hasAvailableParking_VehicleAlreadyParked_throwException() {
        when(parkingGarage.getByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot1").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "ABC1234X")).build(),
                // empty lot
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot2").build(),
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot3").timeIn(LocalDateTime.now())
                        .vehicle(new Vehicle(VehicleType.CAR, "DBC1234X")).build(),
                // empty lot
                ParkingLot.builder().vehicleType(VehicleType.CAR).label("CarLot4").build()
        ));

        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 7, 10, 40, 23);
        try {
            parkingValetService.parkVehicle(VehicleType.CAR, "DBC1234X", timeIn);
            fail("Expect exception to be thrown");
        } catch(Exception e) {
            assertTrue(e instanceof VehicleAlreadyParkedException, e.getClass().getSimpleName() + " is thrown");
            assertEquals("Car DBC1234X is already parked", e.getMessage());
            verify(parkingGarage, times(1)).getByVehicleType(VehicleType.CAR);
        }
    }
}