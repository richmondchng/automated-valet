package org.richmondchng.automatedvalet.exception;

import java.text.MessageFormat;

/**
 * Business logic exception where vehicle is not found in parking.
 *
 * @author richmondchng
 */
public class VehicleNotParkedException extends VehicleParkingException {

    // standard error message
    private static final String MESSAGE = "{0} is not found in parking";

    public VehicleNotParkedException(final String vehicleNumber) {
        super(MessageFormat.format(MESSAGE, vehicleNumber));
    }
}
