package org.richmondchng.automatedvalet.data.repository;

import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.List;

/**
 * Data repository representing parked vehicles.
 *
 * @author richmondchng
 */
public interface ParkedVehicleRepository {

    /**
     * Save parked vehicle details.
     * @param parkedVehicleEntity details
     * @return saved object
     */
    ParkedVehicleEntity save(final ParkedVehicleEntity parkedVehicleEntity);

    /**
     * Get list of parked vehicles by vehicle type. This returns entities that has vehicle number, time in, but without time out.
     * @param vehicleType vehicle type
     * @return list of parked vehicles
     */
    List<ParkedVehicleEntity> getParkedVehicleByVehicleType(final VehicleType vehicleType);
}
