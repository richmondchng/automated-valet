package org.richmondchng.automatedvalet.model.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.Vehicle;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ParkingLot {
    private final VehicleType vehicleType;
    private final String label;
    private Vehicle vehicle;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
}
