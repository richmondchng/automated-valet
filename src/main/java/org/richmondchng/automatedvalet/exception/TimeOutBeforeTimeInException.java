package org.richmondchng.automatedvalet.exception;

/**
 * Business logic exception where vehicle time out is before recorded time in.
 *
 * @author richmondchng
 */
public class TimeOutBeforeTimeInException extends VehicleParkingException {

    public TimeOutBeforeTimeInException() {
        super("Time out is before time in");
    }
}
