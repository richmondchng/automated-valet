package org.richmondchng.automatedvalet.data.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        assertEquals(0, parkedVehicleDataStorage.getParkedVehicles().size());

        final ParkedVehicleEntity result = parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());
        // increase by 1
        assertEquals(1, parkedVehicleDataStorage.getParkedVehicles().size());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals("ABC3456U", result.getVehicleNumber());
        assertEquals(2, result.getLotNumber());
        assertEquals(LocalDateTime.of(2021, 5, 4, 10, 20, 1), result.getTimeIn());
    }

    /**
     * Test save.
     *
     * Valid entity bean, update successful.
     */
    @Test
    void save_validEntity_updateSuccessful() {
        final ParkedVehicleEntity data = parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());

        // update a field
        data.setTimeOut(LocalDateTime.of(2021, 5, 4, 11, 20, 1));
        final ParkedVehicleEntity result = parkedVehicleDataStorage.save(data);
        assertNotNull(result);
        assertEquals(data.getId(), result.getId());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals("ABC3456U", result.getVehicleNumber());
        assertEquals(2, result.getLotNumber());
        assertEquals(LocalDateTime.of(2021, 5, 4, 10, 20, 1), result.getTimeIn());
        assertEquals(LocalDateTime.of(2021, 5, 4, 11, 20, 1), result.getTimeOut());
    }

    /**
     * Test save.
     *
     * Bean Id is invalid, update failed
     */
    @Test
    void save_invalidId_updateFailed() {
        // before save, ensure 0 items in list
        assertEquals(0, parkedVehicleDataStorage.getParkedVehicles().size());

        final UUID id = UUID.randomUUID();
        try {
            parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                    .id(id)
                    .vehicleType(VehicleType.CAR)
                    .vehicleNumber("ABC3456U")
                    .lotNumber(2)
                    .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                    .build());
        } catch (RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("Id " + id + " is invalid", e.getMessage());
        }
        assertEquals(0, parkedVehicleDataStorage.getParkedVehicles().size());
    }

    /**
     * Test getParkedVehicles.
     *
     * Return empty list.
     */
    @Test
    void getParkedVehicles_noBeans_returnEmptyList() {
        final List<ParkedVehicleEntity> results = parkedVehicleDataStorage.getParkedVehicles();
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    /**
     * Test getParkedVehicles.
     *
     * Test unable to modify list
     */
    @Test
    void getParkedVehicles_addItemToList_throwException() {
        final List<ParkedVehicleEntity> results = parkedVehicleDataStorage.getParkedVehicles();
        try {
            results.add(ParkedVehicleEntity.builder().build());
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Test getParkedVehicles.
     *
     * Test unable to modify list
     */
    @Test
    void getParkedVehicles_removeItemFromList_throwException() {
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());

        final List<ParkedVehicleEntity> results = parkedVehicleDataStorage.getParkedVehicles();
        assertEquals(1, results.size());

        try {
            results.remove(0);
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Test getParkedVehicleByVehicleNumber.
     *
     * Empty parameter, throw exception.
     */
    @Test
    void getParkedVehicleByVehicleNumber_emptyParam_throwException() {
        try {
            parkedVehicleDataStorage.getParkedVehicleByVehicleNumber(null);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("Vehicle number cannot be null", e.getMessage());
        }
    }

    /**
     * Test getParkedVehicleByVehicleNumber.
     *
     * Has no matching vehicle number, return null.
     */
    @Test
    void getParkedVehicleByVehicleNumber_noMatch_returnNull() {
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.MOTORCYCLE)
                .vehicleNumber("MMM3456U")
                .lotNumber(1)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());

        final ParkedVehicleEntity result = parkedVehicleDataStorage.getParkedVehicleByVehicleNumber("GGG124");
        assertNull(result);
    }

    /**
     * Test getParkedVehicleByVehicleNumber.
     *
     * Has matching vehicle number, return bean.
     */
    @Test
    void getParkedVehicleByVehicleNumber_matchedVehicleNumber_returnBean() {
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.MOTORCYCLE)
                .vehicleNumber("MMM3456U")
                .lotNumber(1)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());

        final ParkedVehicleEntity result = parkedVehicleDataStorage.getParkedVehicleByVehicleNumber("MMM3456U");
        assertNotNull(result);
        assertEquals(VehicleType.MOTORCYCLE, result.getVehicleType());
        assertEquals("MMM3456U", result.getVehicleNumber());
        assertEquals(1, result.getLotNumber());
    }

    /**
     * Test getRecordById.
     *
     * Empty parameter, throw exception.
     */
    @Test
    void getRecordById_emptyParam_throwException() {
        try {
            parkedVehicleDataStorage.getRecordById(null);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals("Id cannot be null", e.getMessage());
        }
    }

    /**
     * Test getRecordById.
     *
     * Has no matching Id, return null.
     */
    @Test
    void getRecordById_noMatch_returnNull() {
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .timeOut(LocalDateTime.of(2021, 5, 4, 11, 20, 1))
                .build());
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.MOTORCYCLE)
                .vehicleNumber("MMM3456U")
                .lotNumber(1)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .timeOut(LocalDateTime.of(2021, 5, 4, 11, 20, 1))
                .build());

        final ParkedVehicleEntity result = parkedVehicleDataStorage.getRecordById(UUID.randomUUID());
        assertNull(result);
    }

    /**
     * Test getRecordById.
     *
     * Has matching Id, return bean.
     */
    @Test
    void getRecordById_matchedId_returnBean() {
        parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .timeOut(LocalDateTime.of(2021, 5, 4, 11, 20, 1))
                .build());
        final ParkedVehicleEntity b2 = parkedVehicleDataStorage.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.MOTORCYCLE)
                .vehicleNumber("MMM3456U")
                .lotNumber(1)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .timeOut(LocalDateTime.of(2021, 5, 4, 11, 20, 1))
                .build());

        final ParkedVehicleEntity result = parkedVehicleDataStorage.getRecordById(b2.getId());
        assertNotNull(result);
        assertEquals(VehicleType.MOTORCYCLE, result.getVehicleType());
        assertEquals("MMM3456U", result.getVehicleNumber());
        assertEquals(1, result.getLotNumber());
    }
}