package org.richmondchng.automatedvalet.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.dto.response.ParkedDTO;
import org.richmondchng.automatedvalet.model.parking.ParkingLot;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;
import org.richmondchng.automatedvalet.service.ParkingValetService;

import java.time.LocalDateTime;

/**
 * Parking valet controller.
 *
 * @author richmondchng
 */
@RequiredArgsConstructor
public class ParkingValetController {

    private final ParkingValetService parkingValetService;

    /**
     * Vehicle entering parking.
     * @param vehicle Vehicle Type
     * @param licensePlate vehicle license plate
     * @param timestamp time stamp entering parking
     * @return ParkedDTO with parking lot details
     */
    public ParkedDTO enterParking(final VehicleType vehicle, final String licensePlate, final LocalDateTime timestamp) {

        final ParkingLot parked = parkingValetService.parkVehicle(vehicle, licensePlate, timestamp);
        if(parked != null) {
            // a parking lot is allocated
            return new ParkedDTO(true, parked.getLabel());
        }
        // no parking lot allocated
        return new ParkedDTO(false, null);
    }

}
