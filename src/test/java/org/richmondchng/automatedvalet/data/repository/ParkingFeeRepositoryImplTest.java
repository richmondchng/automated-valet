package org.richmondchng.automatedvalet.data.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.richmondchng.automatedvalet.data.entity.ParkingFeeEntity;
import org.richmondchng.automatedvalet.data.storage.ParkingFeeDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test ParkingFeeRepositoryImpl.
 *
 * @author richmondchng
 */
@ExtendWith(MockitoExtension.class)
class ParkingFeeRepositoryImplTest {

    @Mock
    private ParkingFeeDataStorage parkingFeeDataStorage;

    // test instance
    private ParkingFeeRepositoryImpl parkingFeeRepository;

    @BeforeEach
    void setUp() {
        parkingFeeRepository = new ParkingFeeRepositoryImpl(parkingFeeDataStorage);
    }

    @AfterEach
    void tearDown() {
        parkingFeeRepository = null;
    }

    /**
     * Test findByVehicleType.
     *
     * No matching entity, return null.
     */
    @Test
    void findByVehicleType_noMatchedEntity_returnNull() {
        when(parkingFeeDataStorage.getParkingFeeByVehicleType(any(VehicleType.class))).thenReturn(null);

        final ParkingFeeEntity result = parkingFeeRepository.findByVehicleType(VehicleType.MOTORCYCLE);
        assertNull(result);

        verify(parkingFeeDataStorage, times(1)).getParkingFeeByVehicleType(VehicleType.MOTORCYCLE);
    }

    /**
     * Test findByVehicleType.
     *
     * Has matching entity, return entity.
     */
    @Test
    void findByVehicleType_hasMatchedEntity_returnEntityBean() {
        when(parkingFeeDataStorage.getParkingFeeByVehicleType(any(VehicleType.class))).thenReturn(ParkingFeeEntity.builder()
                .vehicleType(VehicleType.MOTORCYCLE)
                .parkingFeePerHour(1)
                .build());

        final ParkingFeeEntity result = parkingFeeRepository.findByVehicleType(VehicleType.MOTORCYCLE);
        assertNotNull(result);
        assertEquals(VehicleType.MOTORCYCLE, result.getVehicleType());
        assertEquals(1, result.getParkingFeePerHour());

        verify(parkingFeeDataStorage, times(1)).getParkingFeeByVehicleType(VehicleType.MOTORCYCLE);
    }
}