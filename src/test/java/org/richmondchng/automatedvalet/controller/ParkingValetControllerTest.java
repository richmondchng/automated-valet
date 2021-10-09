package org.richmondchng.automatedvalet.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.richmondchng.automatedvalet.dto.response.ParkedDTO;
import org.richmondchng.automatedvalet.dto.response.ParkingFeeDTO;
import org.richmondchng.automatedvalet.model.parking.ParkingDetails;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;
import org.richmondchng.automatedvalet.service.ParkingFeeService;
import org.richmondchng.automatedvalet.service.ParkingValetService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test ParkingValetController.
 */
@ExtendWith(MockitoExtension.class)
class ParkingValetControllerTest {

    @Mock
    private ParkingValetService parkingValetService;
    @Mock
    private ParkingFeeService parkingFeeService;

    // test instance
    private ParkingValetController parkingValetController;

    @BeforeEach
    void setUp() {
        parkingValetController = new ParkingValetController(parkingValetService, parkingFeeService);
    }

    @AfterEach
    void tearDown() {
        parkingValetController = null;
    }

    /**
     * Test enterParking.
     *
     * Test parking lot is available. Return success DTO.
     */
    @Test
    void enterParking_parkingAvailable_returnSuccess() {
        final String vehicleNumber = "ABC1234Z";
        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 8, 13, 10, 11);
        final ParkingDetails parked = ParkingDetails.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber(vehicleNumber)
                .timeIn(timeIn)
                .label("CarLot1")
                .build();
        doReturn(parked).when(parkingValetService)
                .parkVehicle(any(VehicleType.class), anyString(), any(LocalDateTime.class));

        final ParkedDTO result = parkingValetController.enterParking(VehicleType.CAR, vehicleNumber, timeIn);

        verify(parkingValetService, times(1)).parkVehicle(VehicleType.CAR, vehicleNumber, timeIn);

        assertNotNull(result);
        assertTrue(result.isAccepted());
        assertEquals("CarLot1", result.getLotNumber());
    }

    /**
     * Test enterParking.
     *
     * Test parking lot is not available. Return unsuccessful DTO.
     */
    @Test
    void enterParking_parkingNotAvailable_returnFailed() {
        final String vehicleNumber = "ABC1234Z";
        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 8, 13, 10, 11);
        when(parkingValetService.parkVehicle(any(VehicleType.class), anyString(), any(LocalDateTime.class))).thenReturn(null);

        final ParkedDTO result = parkingValetController.enterParking(VehicleType.CAR, vehicleNumber, timeIn);

        verify(parkingValetService, times(1)).parkVehicle(VehicleType.CAR, vehicleNumber, timeIn);

        assertNotNull(result);
        assertFalse(result.isAccepted());
        assertNull(result.getLotNumber());
    }

    /**
     * Test exitParking.
     *
     * Vehicle found, return success, calculated parking fees.
     */
    @Test
    void exitParking_vehicleFound_returnSuccess() {
        final String vehicleNumber = "ABC1234Z";
        final LocalDateTime timeOut = LocalDateTime.of(2021, 10, 8, 13, 10, 11);
        when(parkingValetService.removeVehicle(anyString(), any(LocalDateTime.class))).thenReturn(ParkingDetails.builder()
                        .vehicleType(VehicleType.CAR)
                        .vehicleNumber(vehicleNumber)
                        .label("CarLot2")
                        .timeIn(LocalDateTime.of(2021, 10, 8, 11, 10, 11))
                        .timeOut(timeOut).build());
        when(parkingFeeService.calculateParkingFee(any(ParkingDetails.class))).thenReturn(4);

        final ParkingFeeDTO result = parkingValetController.exitParking(vehicleNumber, timeOut);

        verify(parkingValetService, times(1)).removeVehicle(vehicleNumber, timeOut);
        verify(parkingFeeService, times(1)).calculateParkingFee(any(ParkingDetails.class));

        assertNotNull(result);
        assertEquals("CarLot2", result.getLabel());
        assertEquals(4, result.getParkingFee());
    }

    /**
     * Test exitParking.
     *
     * Issue removing vehicle from parking, throws exception.
     */
    @Test
    void exitParking_vehicleNotFound_throwException() {
        final String vehicleNumber = "ABC1234Z";
        final LocalDateTime timeOut = LocalDateTime.of(2021, 10, 8, 13, 10, 11);
        when(parkingValetService.removeVehicle(anyString(), any(LocalDateTime.class))).thenThrow(new RuntimeException());

        try {
            parkingValetController.exitParking(vehicleNumber, timeOut);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            verify(parkingValetService, times(1)).removeVehicle(vehicleNumber, timeOut);
            verifyNoInteractions(parkingFeeService);
        }
    }
}