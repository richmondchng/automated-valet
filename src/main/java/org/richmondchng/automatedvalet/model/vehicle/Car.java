package org.richmondchng.automatedvalet.model.vehicle;

public class Car extends Vehicle{

    public Car(final String licensePlate) {
        super(licensePlate);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.CAR;
    }
}
