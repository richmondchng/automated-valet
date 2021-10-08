package org.richmondchng.automatedvalet.exception;

import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.text.MessageFormat;

/**
 * Exception where a vehicle is already parked.
 * @author richmondchng
 */
public class VehicleAlreadyParkedException extends VehicleParkingException {

    // standard error message
    private static final String MESSAGE = "{0} {1} is already parked";

    public VehicleAlreadyParkedException(final VehicleType vehicleType, final String licensePlate) {
        super(MessageFormat.format(MESSAGE, vehicleType.getLabel(), licensePlate));
    }
}
