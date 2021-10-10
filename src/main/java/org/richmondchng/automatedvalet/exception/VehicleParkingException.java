package org.richmondchng.automatedvalet.exception;

/**
 * Abstract vehicle parking exception. Business logic exception should extend this.
 *
 * @author richmondchng
 */
public abstract class VehicleParkingException extends RuntimeException {

    public VehicleParkingException(final String message, final Throwable e) {
        super(message, e);
    }

    public VehicleParkingException(final String message) {
        super(message);
    }
}
