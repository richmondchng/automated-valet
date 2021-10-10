package org.richmondchng.automatedvalet.exception;

/**
 * Business logic exception where parking timestamps are not recorded.
 * @author richmondchng
 */
public class ParkingTimeNotRecordedException extends VehicleParkingException {

    // standard error message
    private static final String MESSAGE = "Parking times are not recorded";

    public ParkingTimeNotRecordedException() {
        super(MESSAGE);
    }
}
