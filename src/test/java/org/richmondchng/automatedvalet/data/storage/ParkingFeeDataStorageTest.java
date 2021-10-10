package org.richmondchng.automatedvalet.data.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richmondchng.automatedvalet.data.entity.ParkingFeeEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ParkingFeeDataStorageTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Test getParkingFeeByVehicleType.
     *
     * No matching vehicle type, return null.
     */
    @Test
    void getParkingFeeByVehicleType_noMatchingType_returnNull() {
        final Map<VehicleType, Integer> config = new HashMap<>();
        config.put(VehicleType.CAR, 2);
        final ParkingFeeDataStorage parkingFeeDataStorage = new ParkingFeeDataStorage(config);

        final ParkingFeeEntity result = parkingFeeDataStorage.getParkingFeeByVehicleType(VehicleType.MOTORCYCLE);
        assertNull(result);
    }

    /**
     * Test getParkingFeeByVehicleType.
     *
     * Has matching vehicle type, return entity bean.
     */
    @Test
    void getParkingFeeByVehicleType_hasMatchingType_returnEntityBean() {
        final Map<VehicleType, Integer> config = new HashMap<>();
        config.put(VehicleType.CAR, 2);
        config.put(VehicleType.MOTORCYCLE, 1);
        final ParkingFeeDataStorage parkingFeeDataStorage = new ParkingFeeDataStorage(config);

        final ParkingFeeEntity result = parkingFeeDataStorage.getParkingFeeByVehicleType(VehicleType.MOTORCYCLE);
        assertNotNull(result);
        assertEquals(VehicleType.MOTORCYCLE, result.getVehicleType());
        assertEquals(1, result.getParkingFeePerHour());
    }
}