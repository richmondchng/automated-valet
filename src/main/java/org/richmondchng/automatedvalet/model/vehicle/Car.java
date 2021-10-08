package org.richmondchng.automatedvalet.model.vehicle;

public class Car extends Vehicle{

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.CAR;
    }
}
