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
     * Get parking lots by vehicle type, order by lot number ascending.
     * @param vehicleType
     * @return
     */
    List<ParkingLotEntity> finalAllParkingLotsByVehicleTypeOrderByLotNumber(final VehicleType vehicleType);
}
