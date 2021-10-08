package org.richmondchng.automatedvalet.service;

import lombok.AllArgsConstructor;
import org.richmondchng.automatedvalet.model.parking.ParkingLot;
import org.richmondchng.automatedvalet.model.vehicle.Vehicle;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;

/**
 * Parking valet service.
 *
 * @author richmondchng
 */
public class ParkingValetService {

    /**
     * Park vehicle.
     * @param vehicle vehicle type
     * @param licensePlate license plate
     * @param timestamp timestamp entering parking
     * @return ParkingLot describing parking lot details, null if not parked
     */
    public ParkingLot parkVehicle(final VehicleType vehicle, final String licensePlate, final LocalDateTime timestamp) {
        return null;
    }
}
