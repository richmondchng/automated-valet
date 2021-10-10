package org.richmondchng.automatedvalet.data.repository;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.storage.ParkedVehicleDataStorage;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation for ParkedVehicleRepositoryImpl.
 *
 * This uses "in-memory" Java object data storage.
 *
 * @author richmondchng
 */
@RequiredArgsConstructor
public class ParkedVehicleRepositoryImpl implements ParkedVehicleRepository {

    private final ParkedVehicleDataStorage parkedVehicleDataStorage;

    @Override
    public ParkedVehicleEntity save(final ParkedVehicleEntity parkedVehicleEntity) {
        // return a copy so that we don't inadvertently modify the actual data
        return copy(parkedVehicleDataStorage.save(parkedVehicleEntity));
    }

    @Override
    public List<ParkedVehicleEntity> findAllParkedVehicles() {
        return parkedVehicleDataStorage.getParkedVehicles().stream()
                // return a copy so that we don't inadvertently modify the actual data
                .map(b -> copy(b)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ParkedVehicleEntity findParkedVehicleByVehicleNumber(final String vehicleNumber) {
        final ParkedVehicleEntity entity = parkedVehicleDataStorage.getParkedVehicleByVehicleNumber(vehicleNumber);
        if(entity != null) {
            // return a copy so that we don't inadvertently modify the actual data
            return copy(entity);
        }
        return null;
    }

    @Override
    public ParkedVehicleEntity findById(final UUID id) {
        final ParkedVehicleEntity entity = parkedVehicleDataStorage.getRecordById(id);
        if(entity != null) {
            // return a copy so that we don't inadvertently modify the actual data
            return copy(entity);
        }
        return null;
    }

    /**
     * Make a copy
     * @param src ParkedVehicleEntity
     * @return ParkedVehicleEntity
     */
    private ParkedVehicleEntity copy(final ParkedVehicleEntity src) {
        return ParkedVehicleEntity.builder()
                .id(src.getId())
                .vehicleType(src.getVehicleType())
                .vehicleNumber(src.getVehicleNumber())
                .lotNumber(src.getLotNumber())
                .timeIn(src.getTimeIn())
                .timeOut(src.getTimeOut())
                .parkingFee(src.getParkingFee())
                .build();
    }

}
