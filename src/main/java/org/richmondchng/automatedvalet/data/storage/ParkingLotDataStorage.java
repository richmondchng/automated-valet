package org.richmondchng.automatedvalet.data.storage;

import org.richmondchng.automatedvalet.data.entity.ParkingLotEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Mock data storage representing data storage for ParkingLotEntity.
 *
 * @author richmondchng
 */
public class ParkingLotDataStorage {

    private final Map<VehicleType, List<ParkingLotEntity>> parkingLots;

    /**
     * Constructor.
     * @param configurations map containing key-value pair describing number of lots by vehicle type.
     */
    public ParkingLotDataStorage(final Map<VehicleType, Integer> configurations) {
        super();

        if(configurations == null) {
            throw new InvalidParameterException("Configurations is null");
        }
        final Map<VehicleType, List<ParkingLotEntity>> map = new HashMap<>(configurations.size());
        for(Map.Entry<VehicleType, Integer> configuration : configurations.entrySet()) {
            final List<ParkingLotEntity> parkingList = new LinkedList<>();
            for(int index = 1; index <= configuration.getValue(); index++) {
                parkingList.add(ParkingLotEntity.builder()
                        .vehicleType(configuration.getKey())
                        .lotNumber(index)
                        .build());
            }
            map.put(configuration.getKey(), Collections.unmodifiableList(parkingList));
        }
        this.parkingLots = Collections.unmodifiableMap(map);
    }

    /**
     * Get list of parking lots for vehicle type.
     * @param vehicleType vehicle type
     * @return list of parking lots, or empty list if not found
     */
    public List<ParkingLotEntity> getByVehicleType(final VehicleType vehicleType) {
        if(vehicleType == null) {
            throw new InvalidParameterException("Vehicle type cannot be null");
        }
        List<ParkingLotEntity> results = parkingLots.get(vehicleType);
        if(results == null) {
            return Collections.emptyList();
        }
        return results;
    }
}
