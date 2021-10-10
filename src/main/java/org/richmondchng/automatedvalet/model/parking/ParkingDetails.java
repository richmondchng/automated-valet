package org.richmondchng.automatedvalet.model.parking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service bean describing a parked vehicle.
 *
 * @author richmondchng
 */
@Getter
@Setter
@Builder
public class ParkingDetails {
    private UUID id;
    private VehicleType vehicleType;
    private String label;
    private String vehicleNumber;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
}
