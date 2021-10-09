package org.richmondchng.automatedvalet.data.storage;

import org.junit.jupiter.api.Test;
import org.richmondchng.automatedvalet.data.entity.ParkingLotEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ParkingLotDataStorageTest {


    /**
     * Test constructor. Test configuration is required.
     */
    @Test
    void constructor_nullConfiguration_throwException() {
        try {
            new ParkingLotDataStorage(null);
            fail("Expect exception to be thrown");
        }catch(RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("Configurations is null", e.getMessage());
        }
    }

    /**
     * Test constructor. Test valid configuration.
     */
    @Test
    void constructor_hasConfiguration_returnObject() {
        final Map<VehicleType, Integer> config = new HashMap<>();
        final ParkingLotDataStorage parkingLotDataStorage = new ParkingLotDataStorage(config);
        assertNotNull(parkingLotDataStorage);
    }

    /**
     * Test getByVehicleType. Test list is not modifiable.
     */
    @Test
    void getByVehicleType_addNewParkingLot_throwException() {
        final Map<VehicleType, Integer> config = new HashMap<>();
        config.put(VehicleType.CAR, 1);
        final ParkingLotDataStorage parkingLotDataStorage = new ParkingLotDataStorage(config);
        try {
            parkingLotDataStorage.getByVehicleType(VehicleType.CAR).add(ParkingLotEntity.builder()
                    .vehicleType(VehicleType.CAR).lotNumber(100).build());
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Test getByVehicleType. Test parking lots are created.
     */
    @Test
    void getByVehicleType_getList_returnConfiguredNumberOfParkingLots() {
        final Map<VehicleType, Integer> config = new HashMap<>();
        config.put(VehicleType.CAR, 2);
        final ParkingLotDataStorage parkingLotDataStorage = new ParkingLotDataStorage(config);

        final List<ParkingLotEntity> results = parkingLotDataStorage.getByVehicleType(VehicleType.CAR);
        assertEquals(2, results.size());

        final Iterator<ParkingLotEntity> itr = results.iterator();
        int expectedIndex = 1;
        while(itr.hasNext()) {
            final ParkingLotEntity pl = itr.next();
            assertEquals(VehicleType.CAR, pl.getVehicleType());
            assertEquals(expectedIndex, pl.getLotNumber());
            expectedIndex++;
        }
    }
}