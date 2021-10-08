package org.richmondchng.automatedvalet.service;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.data.ParkingGarage;
import org.richmondchng.automatedvalet.exception.VehicleAlreadyParkedException;
import org.richmondchng.automatedvalet.model.parking.ParkingLot;
import org.richmondchng.automatedvalet.model.vehicle.Vehicle;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Parking valet service.
 *
 * @author richmondchng
 */
@RequiredArgsConstructor
public class ParkingValetService {

    private final ParkingGarage parkingGarage;

    /**
     * Park vehicle.
     * @param vehicleType vehicle type
     * @param vehicleNumber license plate
     * @param timestampIn timestamp entering parking
     * @return ParkingLot describing parking lot details, null if not parked
     */
    public ParkingLot parkVehicle(final VehicleType vehicleType, final String vehicleNumber, final LocalDateTime timestampIn) {
        if(vehicleType == null) {
            throw new InvalidParameterException("Vehicle type is required");
        }
        if(vehicleNumber == null) {
            throw new InvalidParameterException("License plate is required");
        }
        if(timestampIn == null) {
            throw new InvalidParameterException("Time in is required");
        }

        // get list of parking lots for vehicle type
        final List<ParkingLot> parkingLotList = parkingGarage.getByVehicleType(vehicleType);

        // find the first available parking lot
        ParkingLot availableLot = null;
        for(ParkingLot parkingLot : parkingLotList) {
            if(parkingLot.getTimeIn() != null && parkingLot.getVehicle() != null) {
                // parking lot is filled
                if(parkingLot.getVehicle().getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
                    // has same vehicle number
                    throw new VehicleAlreadyParkedException(vehicleType, vehicleNumber);
                }
                continue;
            }
            if(availableLot == null) {
                availableLot = parkingLot;
            }
        }
        if(availableLot != null) {
            // found available parking lot
            availableLot.setTimeIn(timestampIn);
            availableLot.setVehicle(new Vehicle(vehicleType, vehicleNumber));
        }
        return availableLot;
    }
}
