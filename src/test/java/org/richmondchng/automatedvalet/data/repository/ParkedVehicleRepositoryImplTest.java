package org.richmondchng.automatedvalet.data.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.storage.ParkedVehicleDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test ParkedVehicleRepositoryImpl.
 *
 * @author richmondchng
 */
@ExtendWith(MockitoExtension.class)
class ParkedVehicleRepositoryImplTest {

    @Mock
    private ParkedVehicleDataStorage parkedVehicleDataStorage;

    // test instance
    private ParkedVehicleRepositoryImpl parkedVehicleRepository;

    @BeforeEach
    void setUp() {
        parkedVehicleRepository = new ParkedVehicleRepositoryImpl(parkedVehicleDataStorage);
    }

    @AfterEach
    void tearDown() {
        parkedVehicleRepository = null;
    }

    /**
     * Test save.
     *
     * Null object. Throw exception
     */
    @Test
    void save_nullObject_throwException() {
        when(parkedVehicleDataStorage.save(any(ParkedVehicleEntity.class))).thenThrow(new RuntimeException());

        try {
            parkedVehicleRepository.save(null);
        } catch (RuntimeException e) {
            verify(parkedVehicleDataStorage, times(1)).save(any());
        }
    }

    /**
     * Test save.
     *
     * Valid entity bean, save successful.
     */
    @Test
    void save_validEntity_saveSuccessful() {

        // not required to test returned value, we want to test the parameter into the data storage
        parkedVehicleRepository.save(ParkedVehicleEntity.builder()
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .build());

        final ArgumentCaptor<ParkedVehicleEntity> captor = ArgumentCaptor.forClass(ParkedVehicleEntity.class);
        verify(parkedVehicleDataStorage, times(1)).save(captor.capture());

        final ParkedVehicleEntity result = captor.getValue();
        assertNotNull(result);
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals("ABC3456U", result.getVehicleNumber());
        assertEquals(2, result.getLotNumber());
        assertEquals(LocalDateTime.of(2021, 5, 4, 10, 20, 1), result.getTimeIn());
    }

    /**
     * Test getParkedVehicleByVehicleType.
     *
     * Has no parked vehicle, return empty list
     */
    @Test
    void getParkedVehicleByVehicleType_noParkedVehicle_returnEmptyList() {
        when(parkedVehicleDataStorage.getParkedVehiclesByVehicleType(any(VehicleType.class))).thenReturn(Collections.emptyList());

        final List<ParkedVehicleEntity> results = parkedVehicleRepository.findAllParkedVehiclesByVehicleType(VehicleType.CAR);

        verify(parkedVehicleDataStorage, times(1)).getParkedVehiclesByVehicleType(VehicleType.CAR);

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    /**
     * Test getParkedVehicleByVehicleType.
     *
     * Has parked vehicles, return list
     */
    @Test
    void getParkedVehicleByVehicleType_hasParkedVehicles_returnList() {
        when(parkedVehicleDataStorage.getParkedVehiclesByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("ABC1234T").build(),
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("DEF2234U").build()
        ));

        final List<ParkedVehicleEntity> results = parkedVehicleRepository.findAllParkedVehiclesByVehicleType(VehicleType.CAR);

        verify(parkedVehicleDataStorage, times(1)).getParkedVehiclesByVehicleType(VehicleType.CAR);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("ABC1234T", results.get(0).getVehicleNumber());
        assertEquals("DEF2234U", results.get(1).getVehicleNumber());
    }

    /**
     * Test getParkedVehicleByVehicleType.
     *
     * Test modify list, throw exception
     */
    @Test
    void getParkedVehicleByVehicleType_modifyList_throwException() {
        when(parkedVehicleDataStorage.getParkedVehiclesByVehicleType(any(VehicleType.class))).thenReturn(Arrays.asList(
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("ABC1234T").build(),
                ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("DEF2234U").build()
        ));

        final List<ParkedVehicleEntity> results = parkedVehicleRepository.findAllParkedVehiclesByVehicleType(VehicleType.CAR);

        try {
            results.add(ParkedVehicleEntity.builder().vehicleType(VehicleType.CAR).vehicleNumber("ABC1299T").build());
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e instanceof UnsupportedOperationException);
            verify(parkedVehicleDataStorage, times(1)).getParkedVehiclesByVehicleType(VehicleType.CAR);
        }
    }
}