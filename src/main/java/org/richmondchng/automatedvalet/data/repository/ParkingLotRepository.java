package org.richmondchng.automatedvalet.data.repository;

import org.richmondchng.automatedvalet.data.entity.ParkingLotEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.List;

/**
 * Interface for ParkingLot data repository.
 *
 * @author richmondchng
 */
public interface ParkingLotRepository {

    /**
     * Get parkling lots by vehicle type, order by lot number ascending.
     * @param vehicleType
     * @return
     */
    List<ParkingLotEntity> getParkingLotByVehicleTypeOrderByLotNumber(final VehicleType vehicleType);
}
