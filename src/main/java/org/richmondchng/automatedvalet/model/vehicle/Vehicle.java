package org.richmondchng.automatedvalet.model.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Vehicle {
    private String licensePlate;

    public abstract VehicleType getVehicleType();
}
