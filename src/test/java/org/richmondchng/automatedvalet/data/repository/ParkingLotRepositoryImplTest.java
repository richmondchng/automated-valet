package org.richmondchng.automatedvalet.data.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.richmondchng.automatedvalet.data.entity.ParkingLotEntity;
import org.richmondchng.automatedvalet.data.storage.ParkingLotDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test ParkingLotRepositoryImpl.
 *
 * @author richmondchng 
 */
@ExtendWith(MockitoExtension.class)
class ParkingLotRepositoryImplTest {

    @Mock
    private ParkingLotDataStorage parkingLotDataStorage;

    // test instance
    private ParkingLotRepositoryImpl parkingLotRepository;

    @BeforeEach
    void setUp() {
        parkingLotRepository = new ParkingLotRepositoryImpl(parkingLotDataStorage);
    }

    @AfterEach
    void tearDown() {
        parkingLotRepository = null;
    }

    /**
     * Test getParkingLotByVehicleTypeOrderByLotNumber.
     *
     * No results in data storage, return empty list
     */
    @Test
    void getParkingLotByVehicleTypeOrderByLotNumber_noParkingLot_returnEmptyList() {
        // return empty list
        when(parkingLotDataStorage.getByVehicleType(any(VehicleType.class))).thenReturn(Collections.emptyList());

        final List<ParkingLotEntity> results = parkingLotRepository.getParkingLotByVehicleTypeOrderByLotNumber(VehicleType.CAR);
        assertNotNull(results);
        assertEquals(0, results.size());

        verify(parkingLotDataStorage, times(1)).getByVehicleType(VehicleType.CAR);
    }

    /**
     * Test getParkingLotByVehicleTypeOrderByLotNumber.
     *
     * Has results in data storage, return ordered list by lot number ascending.
     */
    @Test
    void getParkingLotByVehicleTypeOrderByLotNumber_hasParkingLot_returnOrderedList() {
        // return empty list
        when(parkingLotDataStorage.getByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(2).build(),
                ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(4).build(),
                ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(1).build(),
                ParkingLotEntity.builder().vehicleType(VehicleType.CAR).lotNumber(3).build()
        ));

        final List<ParkingLotEntity> results = parkingLotRepository.getParkingLotByVehicleTypeOrderByLotNumber(VehicleType.CAR);
        assertNotNull(results);
        assertEquals(4, results.size());

        final Iterator<ParkingLotEntity> iterator = results.iterator();
        final ParkingLotEntity result1 = iterator.next();
        assertEquals(VehicleType.CAR, result1.getVehicleType());
        assertEquals(1, result1.getLotNumber());

        final ParkingLotEntity result2 = iterator.next();
        assertEquals(VehicleType.CAR, result2.getVehicleType());
        assertEquals(2, result2.getLotNumber());

        final ParkingLotEntity result3 = iterator.next();
        assertEquals(VehicleType.CAR, result3.getVehicleType());
        assertEquals(3, result3.getLotNumber());

        final ParkingLotEntity result4 = iterator.next();
        assertEquals(VehicleType.CAR, result4.getVehicleType());
        assertEquals(4, result4.getLotNumber());

        verify(parkingLotDataStorage, times(1)).getByVehicleType(VehicleType.CAR);
    }
}