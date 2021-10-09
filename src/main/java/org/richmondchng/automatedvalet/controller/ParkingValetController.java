package org.richmondchng.automatedvalet.controller;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.dto.response.ParkedDTO;
import org.richmondchng.automatedvalet.dto.response.ParkingFeeDTO;
import org.richmondchng.automatedvalet.model.parking.ParkingLot;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;
import org.richmondchng.automatedvalet.service.ParkingFeeService;
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
    private final ParkingFeeService parkingFeeService;

    /**
     * Vehicle entering parking.
     * @param vehicle Vehicle Type
     * @param vehicleNumber vehicle number
     * @param timestamp time stamp entering parking
     * @return ParkedDTO with parking lot details
     */
    public ParkedDTO enterParking(final VehicleType vehicle, final String vehicleNumber, final LocalDateTime timestamp) {

        final ParkingLot parkingDetails = parkingValetService.parkVehicle(vehicle, vehicleNumber, timestamp);
        if(parkingDetails != null) {
            // a parking lot is allocated
            return new ParkedDTO(true, parkingDetails.getLabel());
        }
        // no parking lot allocated
        return new ParkedDTO(false, null);
    }

    /**
     * Vehicle exiting parking.
     * @param vehicleNumber vehicle number
     * @param timestamp time stamp exiting parking
     * @return ParkingFeeDTO with parking fee details.
     */
    public ParkingFeeDTO exitParking(final String vehicleNumber, final LocalDateTime timestamp) {
        // this returns none-null object
        // exception will be thrown if parking details is incorrect
        final ParkingLot parkingDetails = parkingValetService.removeVehicle(vehicleNumber, timestamp);
        final int parkingFees = parkingFeeService.calculateParkingFee(parkingDetails);
        // return details
        return new ParkingFeeDTO(parkingDetails.getLabel(), parkingFees);
    }

}
