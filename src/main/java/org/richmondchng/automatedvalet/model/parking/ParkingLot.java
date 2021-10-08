package org.richmondchng.automatedvalet.model.parking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.Vehicle;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ParkingLot<T extends Vehicle> {
    private T vehicle;
    private String label;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
}
