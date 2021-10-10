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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
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

        doAnswer(invocationOnMock -> {
            final ParkedVehicleEntity param = invocationOnMock.getArgument(0, ParkedVehicleEntity.class);
            param.setId(UUID.randomUUID());
            return param;
        }).when(parkedVehicleDataStorage).save(any(ParkedVehicleEntity.class));

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
     * Test findAllParkedVehiclesByVehicleType.
     *
     * Has no parked vehicle, return empty list
     */
    @Test
    void findAllParkedVehiclesByVehicleType_noParkedVehicle_returnEmptyList() {
        when(parkedVehicleDataStorage.getParkedVehiclesByVehicleType(any(VehicleType.class))).thenReturn(Collections.emptyList());

        final List<ParkedVehicleEntity> results = parkedVehicleRepository.findAllParkedVehiclesByVehicleType(VehicleType.CAR);

        verify(parkedVehicleDataStorage, times(1)).getParkedVehiclesByVehicleType(VehicleType.CAR);

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    /**
     * Test findAllParkedVehiclesByVehicleType.
     *
     * Has parked vehicles, return list
     */
    @Test
    void findAllParkedVehiclesByVehicleType_hasParkedVehicles_returnList() {
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
     * Test findAllParkedVehiclesByVehicleType.
     *
     * Test modify list, throw exception
     */
    @Test
    void findAllParkedVehiclesByVehicleType_modifyList_throwException() {
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

    /**
     * Test findParkedVehicleByVehicleNumber.
     *
     * Has no matching vehicle number, return null.
     */
    @Test
    void findParkedVehicleByVehicleNumber_noMatch_returnNull() {
        when(parkedVehicleDataStorage.getParkedVehicleByVehicleNumber(anyString())).thenReturn(null);

        final ParkedVehicleEntity result = parkedVehicleRepository.findParkedVehicleByVehicleNumber("ABC1234T");
        verify(parkedVehicleDataStorage, times(1)).getParkedVehicleByVehicleNumber("ABC1234T");
        assertNull(result);
    }

    /**
     * Test findParkedVehicleByVehicleNumber.
     *
     * Has matching vehicle number, return entity bean.
     */
    @Test
    void findParkedVehicleByVehicleNumber_matchedVehicleNumber_returnEntity() {
        when(parkedVehicleDataStorage.getParkedVehicleByVehicleNumber(anyString())).thenReturn(ParkedVehicleEntity.builder()
                .id(UUID.randomUUID())
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .timeOut(LocalDateTime.of(2021, 5, 4, 11, 20, 1))
                .build());

        final ParkedVehicleEntity result = parkedVehicleRepository.findParkedVehicleByVehicleNumber("ABC3456U");
        verify(parkedVehicleDataStorage, times(1)).getParkedVehicleByVehicleNumber("ABC3456U");
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals("ABC3456U", result.getVehicleNumber());
        assertEquals(2, result.getLotNumber());
        assertNotNull(result.getTimeIn());
        assertNotNull(result.getTimeOut());
    }

    /**
     * Test findById.
     *
     * Has no matching ID, return null.
     */
    @Test
    void findById_noMatch_returnNull() {
        when(parkedVehicleDataStorage.getRecordById(any(UUID.class))).thenReturn(null);

        final UUID id = UUID.randomUUID();
        final ParkedVehicleEntity result = parkedVehicleRepository.findById(id);

        verify(parkedVehicleDataStorage, times(1)).getRecordById(id);
        assertNull(result);
    }

    /**
     * Test findById.
     *
     * Has matching ID, return entity bean.
     */
    @Test
    void findById_matchedID_returnEntity() {
        final UUID id = UUID.randomUUID();
        when(parkedVehicleDataStorage.getRecordById(any(UUID.class))).thenReturn(ParkedVehicleEntity.builder()
                .id(id)
                .vehicleType(VehicleType.CAR)
                .vehicleNumber("ABC3456U")
                .lotNumber(2)
                .timeIn(LocalDateTime.of(2021, 5, 4, 10, 20, 1))
                .timeOut(LocalDateTime.of(2021, 5, 4, 11, 20, 1))
                .build());

        final ParkedVehicleEntity result = parkedVehicleRepository.findById(id);
        verify(parkedVehicleDataStorage, times(1)).getRecordById(id);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals("ABC3456U", result.getVehicleNumber());
        assertEquals(2, result.getLotNumber());
        assertNotNull(result.getTimeIn());
        assertNotNull(result.getTimeOut());
    }
}