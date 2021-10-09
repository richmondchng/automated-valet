package org.richmondchng.automatedvalet.model.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;

/**
 * Service bean describing a parked vehicle.
 *
 * @author richmondchng
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ParkingLot {
    private VehicleType vehicleType;
    private String label;
    private String vehicleNumber;
    private LocalDateTime timeIn;
}
