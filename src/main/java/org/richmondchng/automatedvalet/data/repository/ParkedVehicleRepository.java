package org.richmondchng.automatedvalet.data.repository;

import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;

import java.util.List;
import java.util.UUID;

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
     * @return list of parked vehicles
     */
    List<ParkedVehicleEntity> findAllParkedVehicles();

    /**
     * Find vehicle by vehicle number, and time out is null.
     * @param vehicleNumber vehicle number
     * @return ParkedVehicleEntity or null if not found
     */
    ParkedVehicleEntity findParkedVehicleByVehicleNumber(final String vehicleNumber);

    /**
     * Find record by ID.
     * @param id record ID
     * @return ParkedVehicleEntity or null
     */
    ParkedVehicleEntity findById(final UUID id);
}
