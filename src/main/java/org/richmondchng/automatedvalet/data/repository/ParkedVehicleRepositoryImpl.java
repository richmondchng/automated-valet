package org.richmondchng.automatedvalet.data.repository;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.storage.ParkedVehicleDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.List;
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
        // return a copy
        return copy(parkedVehicleDataStorage.save(parkedVehicleEntity));
    }

    @Override
    public List<ParkedVehicleEntity> findAllParkedVehiclesByVehicleType(final VehicleType vehicleType) {
        return parkedVehicleDataStorage.getParkedVehiclesByVehicleType(vehicleType).stream()
                .map(b -> copy(b)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ParkedVehicleEntity findParkedVehicleByVehicleNumber(final String vehicleNumber) {
        final ParkedVehicleEntity entity = parkedVehicleDataStorage.getParkedVehicleByVehicleNumber(vehicleNumber);
        if(entity != null) {
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
