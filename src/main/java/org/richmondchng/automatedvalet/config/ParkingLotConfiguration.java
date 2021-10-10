package org.richmondchng.automatedvalet.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

/**
 * Configuration object containing parking lot details.
 *
 * @author richmondchng
 */
@Getter
@AllArgsConstructor
public class ParkingLotConfiguration {
    private VehicleType vehicleType;
    private int numberOfLots;
    private int feePerHours;
}
