package org.richmondchng.automatedvalet.model.parking;

import lombok.Getter;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.Vehicle;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParkingLot<T extends Vehicle> {
    private T vehicle;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
}
