package org.richmondchng.automatedvalet.model.parking;

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
public class ParkingDetails {
    private VehicleType vehicleType;
    private String label;
    private String vehicleNumber;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
}
