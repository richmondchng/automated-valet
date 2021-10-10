package org.richmondchng.automatedvalet.model.vehicle;

import java.security.InvalidParameterException;

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

    /**
     * Get vehicle type by name.
     * @param name name
     * @return VehicleType
     */
    public static VehicleType getVehicleType(final String name) {
        if(name == null) {
            throw new InvalidParameterException("Input value is null");
        }
        VehicleType vehicleType = null;
        for(VehicleType vt : values()) {
            if(vt.name().equalsIgnoreCase(name)) {
                vehicleType = vt;
                break;
            }
        }
        if(vehicleType == null) {
            throw new IllegalArgumentException("Not a valid vehicle type: " + name);
        }
        return vehicleType;
    }
}
