package org.richmondchng.automatedvalet.data;

import org.richmondchng.automatedvalet.model.parking.ParkingLot;
import org.richmondchng.automatedvalet.model.vehicle.Vehicle;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Data repository for parking lots.
 *
 * @author richmondchng
 */
public class ParkingGarage {

    private final Map<VehicleType, List<ParkingLot>> parkingLots;

    /**
     * Constructor.
     * @param configurations map containing key-value pair describing number of lots by vehicle type.
     */
    public ParkingGarage(final Map<VehicleType, Integer> configurations) {
        super();
        if(configurations == null) {
            throw new InvalidParameterException("Configurations is null");
        }
        final Map<VehicleType, List<ParkingLot>> map = new HashMap<>(configurations.size());
        for(Map.Entry<VehicleType, Integer> configuration : configurations.entrySet()) {
            final List<ParkingLot> parkingList = new LinkedList<>();
            for(int index = 1; index <= configuration.getValue(); index++) {
                parkingList.add(ParkingLot.builder()
                                .vehicleType(configuration.getKey())
                                .label(configuration.getKey().getLabel() + "Lot" + index)
                                .build());
            }
            map.put(configuration.getKey(), Collections.unmodifiableList(parkingList));
        }
        this.parkingLots = Collections.unmodifiableMap(map);
    }

    /**
     * Get list of parking lots for vehicle type.
     * @param vehicleType vehicle type
     * @return list of parking lots
     */
    public List<ParkingLot> getByVehicleType(final VehicleType vehicleType) {
        if(vehicleType == null) {
            throw new InvalidParameterException("Vehicle type cannot be null");
        }
        List<ParkingLot> results = parkingLots.get(vehicleType);
        if(results == null) {
            throw new InvalidParameterException("No lots allocated for " + vehicleType);
        }
        return results;
    }
}
