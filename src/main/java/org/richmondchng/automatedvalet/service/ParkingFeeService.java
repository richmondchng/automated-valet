package org.richmondchng.automatedvalet.service;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.entity.ParkingFeeEntity;
import org.richmondchng.automatedvalet.data.repository.ParkedVehicleRepository;
import org.richmondchng.automatedvalet.data.repository.ParkingFeeRepository;
import org.richmondchng.automatedvalet.exception.ParkingFeeNotConfiguredException;
import org.richmondchng.automatedvalet.exception.ParkingTimeNotRecordedException;
import org.richmondchng.automatedvalet.exception.TimeOutBeforeTimeInException;
import org.richmondchng.automatedvalet.exception.VehicleNotParkedException;
import org.richmondchng.automatedvalet.model.parking.ParkingDetails;
import org.richmondchng.automatedvalet.util.TimeUtil;

import java.security.InvalidParameterException;

/**
 * Service to calculate parking fees.
 *
 * @author richmondchng
 */
@RequiredArgsConstructor
public class ParkingFeeService {

    private final ParkingFeeRepository parkingFeeRepository;
    private final ParkedVehicleRepository parkedVehicleRepository;

    /**
     * Calculate parking fee for supplied parking details.
     * @param parkingDetails parking details
     * @return parking fee
     */
    public long calculateParkingFee(final ParkingDetails parkingDetails) {
        if(parkingDetails == null) {
            throw new InvalidParameterException("Parking details is required");
        }
        if(parkingDetails.getTimeIn() == null || parkingDetails.getTimeOut() == null) {
            // time stamps not recorded
            throw new ParkingTimeNotRecordedException();
        }
        if(parkingDetails.getTimeOut().compareTo(parkingDetails.getTimeIn()) < 0) {
            // time out before time in
            throw new TimeOutBeforeTimeInException();
        }
        final ParkingFeeEntity parkingFeeConfig = parkingFeeRepository.findByVehicleType(parkingDetails.getVehicleType());
        if(parkingFeeConfig == null) {
            // no parking fee configured
            throw new ParkingFeeNotConfiguredException(parkingDetails.getVehicleType());
        }
        final ParkedVehicleEntity parkedVehicle = parkedVehicleRepository.findById(parkingDetails.getId());
        if(parkedVehicle == null) {
            throw new VehicleNotParkedException(parkedVehicle.getVehicleNumber());
        }
        final long hours = TimeUtil.calculateHours(parkingDetails.getTimeIn(), parkingDetails.getTimeOut());
        final long parkingFees = hours * parkingFeeConfig.getParkingFeePerHour();

        // update parking fees
        parkedVehicle.setParkingFee(parkingFees);
        parkedVehicleRepository.save(parkedVehicle);

        return parkingFees;
    }
}
