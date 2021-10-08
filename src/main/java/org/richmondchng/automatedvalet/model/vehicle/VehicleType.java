package org.richmondchng.automatedvalet.model.vehicle;

public enum VehicleType {
    CAR("Car"),
    MOTORCYCLE("Motorcycle")
    ;

    private final String label;
    private VehicleType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
