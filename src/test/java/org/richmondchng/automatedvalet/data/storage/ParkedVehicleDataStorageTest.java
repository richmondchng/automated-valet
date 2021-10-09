package org.richmondchng.automatedvalet.data.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test ParkedVehicleDataStorage.
 *
 * @author richmondchng
 */
class ParkedVehicleDataStorageTest {

    private ParkedVehicleDataStorage parkedVehicleDataStorage;

    @BeforeEach
    void setUp() {
        parkedVehicleDataStorage = new ParkedVehicleDataStorage();
    }

    @AfterEach
    void tearDown() {
        parkedVehicleDataStorage = null;
    }

    /**
     * Test save.
     *
     * Null object. Throw exception
     */
    @Test
    void save_nullObject_throwException() {
        try {
            parkedVehicleDataStorage.save(null);
        } catch (RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("ParkedVehicleEntity cannot be null", e.getMessage());
        }
    }

    /**
     * Test save.
     *
     * Valid entity bean, save successful.
     */
    @Test
    void save_validEntity_saveSuccessful() {
        // before save, ensure 0 items in list
        assertEquals(0, parkedVehicleDataStorage.getParkedVehiclesByVehicleType(VehicleType.CAR).size());

        final ParkedVehicleEntity result = parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());
        // increase by 1
        assertEquals(1, parkedVehicleDataStorage.getParkedVehiclesByVehicleType(VehicleType.CAR).size());

        assertNotNull(result);
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals("ABC3456U", result.getVehicleNumber());
        assertEquals(2, result.getLotNumber());
        assertEquals(LocalDateTime.of(2021, 5, 4, 10, 20, 1), result.getTimeIn());
    }

    /**
     * Test getParkedVehiclesByVehicleType.
     *
     * Return empty list.
     */
    @Test
    void getParkedVehiclesByVehicleType_noBeans_returnEmptyList() {
        final List<ParkedVehicleEntity> results = parkedVehicleDataStorage.getParkedVehiclesByVehicleType(VehicleType.CAR);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    /**
     * Test getParkedVehiclesByVehicleType.
     *
     * Invalid parameter, throw exception
     */
    @Test
    void getParkedVehiclesByVehicleType_nullParameter_throwException() {
        try {
            parkedVehicleDataStorage.getParkedVehiclesByVehicleType(null);
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("VehicleType cannot be null", e.getMessage());
        }
    }

    /**
     * Test getParkedVehiclesByVehicleType.
     *
     * Test unable to modify list
     */
    @Test
    void getParkedVehiclesByVehicleType_addItemToList_throwException() {
        final List<ParkedVehicleEntity> results = parkedVehicleDataStorage.getParkedVehiclesByVehicleType(VehicleType.CAR);
        try {
            results.add(ParkedVehicleEntity.builder().build());
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Test getParkedVehiclesByVehicleType.
     *
     * Test unable to modify list
     */
    @Test
    void getParkedVehiclesByVehicleType_removeItemFromList_throwException() {
        final ParkedVehicleEntity result = parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());

        final List<ParkedVehicleEntity> results = parkedVehicleDataStorage.getParkedVehiclesByVehicleType(VehicleType.CAR);
        assertEquals(1, results.size());

        try {
            results.remove(0);
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }
}