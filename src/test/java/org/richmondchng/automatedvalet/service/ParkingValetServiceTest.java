package org.richmondchng.automatedvalet.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.entity.ParkingLotEntity;
import org.richmondchng.automatedvalet.data.repository.ParkedVehicleRepository;
import org.richmondchng.automatedvalet.data.repository.ParkingLotRepository;
import org.richmondchng.automatedvalet.exception.VehicleAlreadyParkedException;
import org.richmondchng.automatedvalet.model.parking.ParkingLot;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
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
    private ParkingLotRepository parkingLotRepository;
    @Mock
    private ParkedVehicleRepository parkedVehicleRepository;

    // test instance
    private ParkingValetService parkingValetService;

    @BeforeEach
    void setUp() {
        parkingValetService = new ParkingValetService(parkingLotRepository, parkedVehicleRepository);
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
            assertTrue(e instanceof InvalidParameterException, "Expect InvalidParameterException but got " + e.getClass().getSimpleName());
            assertEquals("Vehicle type is required", e.getMessage());
            verifyNoInteractions(parkingLotRepository);
            verifyNoInteractions(parkedVehicleRepository);
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
            assertTrue(e instanceof InvalidParameterException, "Expect InvalidParameterException but got " + e.getClass().getSimpleName());
            assertEquals("License plate is required", e.getMessage());
            verifyNoInteractions(parkingLotRepository);
            verifyNoInteractions(parkedVehicleRepository);
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
            assertTrue(e instanceof InvalidParameterException, "Expect InvalidParameterException but got " + e.getClass().getSimpleName());
            assertEquals("Time in is required", e.getMessage());
            verifyNoInteractions(parkingLotRepository);
            verifyNoInteractions(parkedVehicleRepository);
        }
    }

    /**
     * Test parkVehicle. No available parking lots, return null.
     */
    @Test
    void parkVehicle_noAvailableParking_returnNull() {
        when(parkingLotRepository.getParkingLotByVehicleTypeOrderByLotNumber(any(VehicleType.class))).thenReturn(
                Arrays.asList(
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(1).build(),
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(2).build(),
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(3).build(),
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(4).build()
                ));
        when(parkedVehicleRepository.getParkedVehicleByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("ABC1234X").lotNumber(1)
                        .timeIn(LocalDateTime.now()).build(),
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("DBC2234X").lotNumber(2)
                        .timeIn(LocalDateTime.now()).build(),
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("DBC1234X").lotNumber(3)
                        .timeIn(LocalDateTime.now()).build(),
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("ABC2234X").lotNumber(4)
                        .timeIn(LocalDateTime.now()).build()
        ));

        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 7, 10, 40, 23);
        final ParkingLot result = parkingValetService.parkVehicle(VehicleType.CAR, "YEE4562U", timeIn);

        verify(parkingLotRepository, times(1)).getParkingLotByVehicleTypeOrderByLotNumber(
                VehicleType.CAR);
        verify(parkedVehicleRepository, times(1)).getParkedVehicleByVehicleType(VehicleType.CAR);
        // no save action
        verify(parkedVehicleRepository, never()).save(any(ParkedVehicleEntity.class));

        assertNull(result);
    }

    /**
     * Test parkVehicle. Has available parking lot, but vehicle is already parked.
     */
    @Test
    void parkVehicle_hasAvailableParking_VehicleAlreadyParked_throwException() {
        // vehicle is already parked in lot 2
        when(parkedVehicleRepository.getParkedVehicleByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("ABC1234X").lotNumber(1)
                        .timeIn(LocalDateTime.now()).build(),
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("DBC1234X").lotNumber(3)
                        .timeIn(LocalDateTime.now()).build()
        ));

        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 7, 10, 40, 23);
        try {
            parkingValetService.parkVehicle(VehicleType.CAR, "DBC1234X", timeIn);
            fail("Expect exception to be thrown");
        } catch(Exception e) {
            assertTrue(e instanceof VehicleAlreadyParkedException, e.getClass().getSimpleName() + " is thrown");
            assertEquals("Car DBC1234X is already parked", e.getMessage());
            verifyNoInteractions(parkingLotRepository);
            verify(parkedVehicleRepository, times(1)).getParkedVehicleByVehicleType(VehicleType.CAR);
            verify(parkedVehicleRepository, never()).save(any(ParkedVehicleEntity.class));
        }
    }

    /**
     * Test parkVehicle. Has available parking lots, return parking lot details.
     */
    @Test
    void parkVehicle_hasAvailableParking_returnParkingLot() {
        when(parkingLotRepository.getParkingLotByVehicleTypeOrderByLotNumber(any(VehicleType.class))).thenReturn(
                Arrays.asList(
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(1).build(),
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(2).build(),
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(3).build(),
                        ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(4).build()
                ));
        // lot 2 and 4 are empty
        when(parkedVehicleRepository.getParkedVehicleByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("ABC1234X").lotNumber(1)
                        .timeIn(LocalDateTime.now()).build(),
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("DBC1234X").lotNumber(3)
                        .timeIn(LocalDateTime.now()).build()
        ));
        doAnswer(invocationOnMock -> {
            // return bean
            return invocationOnMock.getArgument(0, ParkedVehicleEntity.class);
        }).when(parkedVehicleRepository).save(any(ParkedVehicleEntity.class));

        final LocalDateTime timeIn = LocalDateTime.of(2021, 10, 7, 10, 40, 23);
        final ParkingLot result = parkingValetService.parkVehicle(VehicleType.CAR, "YEE4562U", timeIn);

        verify(parkingLotRepository, times(1)).getParkingLotByVehicleTypeOrderByLotNumber(
                VehicleType.CAR);
        verify(parkedVehicleRepository, times(1)).getParkedVehicleByVehicleType(VehicleType.CAR);
        // save entity bean
        final ArgumentCaptor<ParkedVehicleEntity> captor = ArgumentCaptor.forClass(ParkedVehicleEntity.class);
        verify(parkedVehicleRepository, times(1)).save(captor.capture());
        final ParkedVehicleEntity captured = captor.getValue();
        assertEquals(VehicleType.CAR, captured.getVehicleType());
        assertEquals("YEE4562U", captured.getVehicleNumber());
        assertEquals(2, captured.getLotNumber());
        assertEquals(timeIn, captured.getTimeIn());

        assertNotNull(result);
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals("YEE4562U", result.getVehicleNumber());
        assertEquals("CarLot2", result.getLabel());
        assertEquals(timeIn, result.getTimeIn());
    }

}