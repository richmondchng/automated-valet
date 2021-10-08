package org.richmondchng.automatedvalet.exception;

public abstract class VehicleParkingException extends RuntimeException {

    public VehicleParkingException(final String message, final Throwable e) {
        super(message, e);
    }

    public VehicleParkingException(final String message) {
        super(message);
    }
}
