package org.richmondchng.automatedvalet.data.repository;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.storage.ParkedVehicleDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.Collections;
import java.util.List;

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
        return parkedVehicleDataStorage.save(parkedVehicleEntity);
    }

    @Override
    public List<ParkedVehicleEntity> findAllParkedVehiclesByVehicleType(final VehicleType vehicleType) {
        return Collections.unmodifiableList(parkedVehicleDataStorage.getParkedVehiclesByVehicleType(vehicleType));
    }

    @Override
    public ParkedVehicleEntity findParkedVehicleByVehicleNumber(final String vehicleNumber) {
        return null;
    }
}
