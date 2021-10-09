package org.richmondchng.automatedvalet.data.storage;

import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Data storage representing data storage for ParkedVehicleEntity.
 *
 * This can then be replaced with actual database, or other data storage.
 *
 * @author richmondchng
 */
public class ParkedVehicleDataStorage {

    private final Map<VehicleType, List<ParkedVehicleEntity>> parkedVehiclesMap;

    public ParkedVehicleDataStorage() {
        parkedVehiclesMap = new HashMap<>();
    }

    /**
     * Save entity bean.
     * @param parkedVehicleEntity ParkedVehicleEntity
     * @return updated bean
     */
    public ParkedVehicleEntity save(final ParkedVehicleEntity parkedVehicleEntity) {
        if(parkedVehicleEntity == null) {
            throw new InvalidParameterException("ParkedVehicleEntity cannot be null");
        }
        List<ParkedVehicleEntity> parkedVehicleEntities = parkedVehiclesMap.get(parkedVehicleEntity.getVehicleType());
        if(parkedVehicleEntities == null) {
            // first entry for vehicle type
            parkedVehicleEntities = new LinkedList<>();
            // put back into map
            parkedVehiclesMap.put(parkedVehicleEntity.getVehicleType(), parkedVehicleEntities);
        }
        parkedVehicleEntities.add(parkedVehicleEntity);
        return parkedVehicleEntity;
    }

    /**
     * Get list of parked vehicles by vehicle type.
     * @param vehicleType vehicle type
     * @return unmodifiable list of ParkedVehicleEntity beans
     */
    public List<ParkedVehicleEntity> getParkedVehiclesByVehicleType(final VehicleType vehicleType) {
        if(vehicleType == null) {
            throw new InvalidParameterException("VehicleType cannot be null");
        }
        List<ParkedVehicleEntity> parkedVehicleEntities = parkedVehiclesMap.getOrDefault(vehicleType, new ArrayList<>());
        // return an unmodifiable list
        return Collections.unmodifiableList(parkedVehicleEntities);
    }
}
