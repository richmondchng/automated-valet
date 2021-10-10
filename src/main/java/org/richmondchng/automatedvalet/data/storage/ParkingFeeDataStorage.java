package org.richmondchng.automatedvalet.data.storage;

import org.richmondchng.automatedvalet.data.entity.ParkingFeeEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.HashMap;
import java.util.Map;

/**
 * Data storage representing data storage for ParkingFeeEntity.
 *
 * This can then be replaced with actual database, or other data storage.
 *
 * @author richmondchng
 */
public class ParkingFeeDataStorage {

    private final Map<VehicleType, ParkingFeeEntity> parkingChargesMap;

    public ParkingFeeDataStorage(final Map<VehicleType, Integer> configurations) {
        parkingChargesMap = new HashMap<>();
        for(Map.Entry<VehicleType, Integer> configuration : configurations.entrySet()) {
            parkingChargesMap.put(configuration.getKey(), ParkingFeeEntity.builder()
                    .vehicleType(configuration.getKey())
                    .parkingFeePerHour(configuration.getValue())
                    .build());
        }
    }

    /**
     * Get parking fee entity by vehicle type.
     * @param vehicleType vehicle type
     * @return ParkingFeeEntity, or null if not found
     */
    public ParkingFeeEntity getParkingFeeByVehicleType(final VehicleType vehicleType) {
        return parkingChargesMap.get(vehicleType);
    }
}
