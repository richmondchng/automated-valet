package org.richmondchng.automatedvalet.exception;

import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.text.MessageFormat;

/**
 * Business logic exception where parking fee is not configured.
 * @author richmondchng
 */
public class ParkingFeeNotConfiguredException extends VehicleParkingException {

    // standard error message
    private static final String MESSAGE = "Parking fee for {0} is not configured";

    public ParkingFeeNotConfiguredException(final VehicleType vehicleType) {
        super(MessageFormat.format(MESSAGE, vehicleType.getLabel()));
    }
}
