package org.richmondchng.automatedvalet.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.entity.ParkingFeeEntity;
import org.richmondchng.automatedvalet.data.repository.ParkedVehicleRepository;
import org.richmondchng.automatedvalet.data.repository.ParkingFeeRepository;
import org.richmondchng.automatedvalet.exception.ParkingFeeNotConfiguredException;
import org.richmondchng.automatedvalet.exception.ParkingTimeNotRecordedException;
import org.richmondchng.automatedvalet.exception.TimeOutBeforeTimeInException;
import org.richmondchng.automatedvalet.exception.VehicleParkingException;
import org.richmondchng.automatedvalet.model.parking.ParkingDetails;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test ParkingFeeService.
 *
 * @author richmondchng
 */
@ExtendWith(MockitoExtension.class)
class ParkingFeeServiceTest {

    @Mock
    private ParkingFeeRepository parkingFeeRepository;
    @Mock
    private ParkedVehicleRepository parkedVehicleRepository;

    // test instance
    private ParkingFeeService parkingFeeService;

    @BeforeEach
    void setUp() {
        parkingFeeService = new ParkingFeeService(parkingFeeRepository, parkedVehicleRepository);
    }

    @AfterEach
    void tearDown() {
        parkingFeeService = null;
    }

    /**
     * Test calculateParkingFee.
     *
     * No parameter, throw exception.
     */
    @Test
    void calculateParkingFee_nullParkingDetails_throwException() {
        try {
            parkingFeeService.calculateParkingFee(null);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("Parking details is required", e.getMessage());
            verifyNoInteractions(parkingFeeRepository);
            verifyNoInteractions(parkedVehicleRepository);
        }
    }

    /**
     * Test calculateParkingFee.
     *
     * Has no parking fee configured, throw exception.
     */
    @Test
    void calculateParkingFee_noEntityForVehicleType_throwException() {
        when(parkingFeeRepository.findByVehicleType(any(VehicleType.class))).thenReturn(null);

        final ParkingDetails input = ParkingDetails.builder()
                .id(UUID.randomUUID())
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC1234")
                .timeIn(LocalDateTime.of(2021, 10, 9, 10, 30, 20))
                .timeOut(LocalDateTime.of(2021, 10, 9, 11, 30, 20))
                .build();
        try {
            parkingFeeService.calculateParkingFee(input);
            fail("Expect exception to be thrown");
        } catch(VehicleParkingException e) {
            assertTrue(e instanceof ParkingFeeNotConfiguredException, "Exception thrown " + e.getClass().getSimpleName());
            assertEquals("Parking fee for Car is not configured", e.getMessage());
            verify(parkingFeeRepository, times(1)).findByVehicleType(VehicleType.CAR);
            verifyNoInteractions(parkedVehicleRepository);
        }
    }

    /**
     * Test calculateParkingFee.
     *
     * Time stamps not recorded, throw exception.
     */
    @Test
    void calculateParkingFee_noTimestamps_throwException() {
        final ParkingDetails input = ParkingDetails.builder()
                .id(UUID.randomUUID())
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC1234")
                .timeIn(LocalDateTime.of(2021, 10, 9, 10, 30, 20))
                .build();
        try {
            parkingFeeService.calculateParkingFee(input);
            fail("Expect exception to be thrown");
        } catch(VehicleParkingException e) {
            assertTrue(e instanceof ParkingTimeNotRecordedException);
            verifyNoInteractions(parkingFeeRepository);
            verifyNoInteractions(parkedVehicleRepository);
        }
    }

    /**
     * Test calculateParkingFee.
     *
     * Time out before time in, throw exception.
     */
    @Test
    void calculateParkingFee_timeOutIsBeforeTimeIn_throwException() {
        final ParkingDetails input = ParkingDetails.builder()
                .id(UUID.randomUUID())
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC1234")
                .timeIn(LocalDateTime.of(2021, 10, 9, 10, 30, 20))
                .timeOut(LocalDateTime.of(2021, 10, 9, 10, 28, 20))
                .build();
        try {
            parkingFeeService.calculateParkingFee(input);
            fail("Expect exception to be thrown");
        } catch(VehicleParkingException e) {
            assertTrue(e instanceof TimeOutBeforeTimeInException, "Exception thrown " + e.getClass().getSimpleName());
            verifyNoInteractions(parkingFeeRepository);
            verifyNoInteractions(parkedVehicleRepository);
        }
    }

    /**
     * Test calculateParkingFee.
     *
     * Calculate parking fee.
     */
    @Test
    void calculateParkingFee_validObject_calculateParkingFee() {
        final UUID id = UUID.randomUUID();
        when(parkingFeeRepository.findByVehicleType(any(VehicleType.class))).thenReturn(ParkingFeeEntity.builder()
                .vehicleType(VehicleType.CAR).parkingFeePerHour(4).build());
        when(parkedVehicleRepository.findById(any(UUID.class))).thenReturn(ParkedVehicleEntity.builder()
                .id(id)
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC1234")
                .timeIn(LocalDateTime.of(2021, 10, 9, 10, 30, 20))
                .timeOut(LocalDateTime.of(2021, 10, 9, 12, 30, 20))
                .build());

        final ParkingDetails input = ParkingDetails.builder()
                .id(id)
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC1234")
                .timeIn(LocalDateTime.of(2021, 10, 9, 10, 30, 20))
                .timeOut(LocalDateTime.of(2021, 10, 9, 12, 30, 20))
                .build();

        final long result = parkingFeeService.calculateParkingFee(input);

        verify(parkingFeeRepository, times(1)).findByVehicleType(VehicleType.CAR);
        verify(parkedVehicleRepository, times(1)).findById(id);
        final ArgumentCaptor<ParkedVehicleEntity> captor = ArgumentCaptor.forClass(ParkedVehicleEntity.class);
        verify(parkedVehicleRepository, times(1)).save(captor.capture());
        final ParkedVehicleEntity entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("ABC1234", entity.getVehicleNumber());
        assertEquals(8, entity.getParkingFee());

        // 2 hours x $4 = $8
        assertEquals(8, result);
    }
}