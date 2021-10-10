package org.richmondchng.automatedvalet.model.vehicle;

/**
 * Enumeration describing the different type of vehicle available for valet service.
 *
 * @author richmondchng
 */
public enum VehicleType {
    CAR("Car"),
    MOTORCYCLE("Motorcycle")
    ;

    private final String label;

    VehicleType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
