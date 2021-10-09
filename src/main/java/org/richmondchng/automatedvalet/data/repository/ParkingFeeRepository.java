package org.richmondchng.automatedvalet.data.repository;

import org.richmondchng.automatedvalet.data.entity.ParkingFeeEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

/**
 * Data repository representing parking fee configurations.
 *
 * @author richmondchng
 */
public interface ParkingFeeRepository {

    /**
     * Get parking fee details by vehicle type.
     * @param vehicleType Vehicle Type
     * @return ParkingFeeEntity or null
     */
    ParkingFeeEntity findByVehicleType(final VehicleType vehicleType);
}
