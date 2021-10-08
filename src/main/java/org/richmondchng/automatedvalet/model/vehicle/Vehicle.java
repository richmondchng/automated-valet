package org.richmondchng.automatedvalet.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Vehicle {
    private String licensePlate;

    public abstract VehicleType getVehicleType();
}
