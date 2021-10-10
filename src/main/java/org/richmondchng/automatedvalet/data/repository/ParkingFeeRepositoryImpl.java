package org.richmondchng.automatedvalet.data.repository;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.data.entity.ParkingFeeEntity;
import org.richmondchng.automatedvalet.data.storage.ParkingFeeDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

/**
 * Implementation for ParkingFeeRepositor.
 *
 * This uses "in-memory" Java object data storage.
 *
 * @author richmondchng
 */
@RequiredArgsConstructor
public class ParkingFeeRepositoryImpl implements ParkingFeeRepository {

    private final ParkingFeeDataStorage parkingFeeDataStorage;

    @Override
    public ParkingFeeEntity findByVehicleType(final VehicleType vehicleType) {

        final ParkingFeeEntity entity = parkingFeeDataStorage.getParkingFeeByVehicleType(vehicleType);
        if(entity != null) {
            // return a copy so that we don't inadvertently modify the actual data
            return copy(entity);
        }
        return null;
    }

    /**
     * Create a copy
     * @param src source
     * @return copy of src
     */
    private ParkingFeeEntity copy(final ParkingFeeEntity src) {
        return ParkingFeeEntity.builder()
                .vehicleType(src.getVehicleType())
                .parkingFeePerHour(src.getParkingFeePerHour())
                .build();
    }
}
