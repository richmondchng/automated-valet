package org.richmondchng.automatedvalet.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richmondchng.automatedvalet.model.parking.ParkingLot;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test ParkingGarage.
 *
 * @author richmondchng
 */
class ParkingGarageTest {

    /**
     * Test constructor. Test configuration is required.
     */
    @Test
    void constructor_nullConfiguration_throwException() {
        try {
            new ParkingGarage(null);
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
        final ParkingGarage parkingGarage = new ParkingGarage(config);
        assertNotNull(parkingGarage);
    }

    /**
     * Test getByVehicleType. Test list is not modifiable.
     */
    @Test
    void getByVehicleType_addNewParkingLot_throwException() {
        final Map<VehicleType, Integer> config = new HashMap<>();
        config.put(VehicleType.CAR, 1);
        final ParkingGarage parkingGarage = new ParkingGarage(config);
        try {
            parkingGarage.getByVehicleType(VehicleType.CAR).add(new ParkingLot(VehicleType.CAR, "test"));
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
        final ParkingGarage parkingGarage = new ParkingGarage(config);

        final List<ParkingLot> results = parkingGarage.getByVehicleType(VehicleType.CAR);
        assertEquals(2, results.size());

        final Iterator<ParkingLot> itr = results.iterator();
        int expectedIndex = 1;
        while(itr.hasNext()) {
            final ParkingLot pl = itr.next();
            assertEquals(VehicleType.CAR, pl.getVehicleType());
            assertEquals("CarLot" + expectedIndex, pl.getLabel());
            assertNull(pl.getVehicle());
            assertNull(pl.getTimeIn());
            expectedIndex++;
        }
    }
}