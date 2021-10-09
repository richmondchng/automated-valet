package org.richmondchng.automatedvalet.service;

import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.data.entity.ParkingLotEntity;
import org.richmondchng.automatedvalet.data.repository.ParkedVehicleRepository;
import org.richmondchng.automatedvalet.data.repository.ParkingLotRepository;
import org.richmondchng.automatedvalet.exception.TimeOutBeforeTimeInException;
import org.richmondchng.automatedvalet.exception.VehicleAlreadyParkedException;
import org.richmondchng.automatedvalet.exception.VehicleNotParkedException;
import org.richmondchng.automatedvalet.model.parking.ParkingDetails;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Parking valet service.
 *
 * @author richmondchng
 */
@RequiredArgsConstructor
public class ParkingValetService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkedVehicleRepository parkedVehicleRepository;

    // parking lot label e.g. CarLot1, CarLot2, etc..., MotorcycleLot1, MotorcycleLot2, etc...
    private static final String PARKING_LOT_LABEL = "{0}Lot{1}";

    /**
     * Park vehicle.
     * @param vehicleType vehicle type
     * @param vehicleNumber license plate
     * @param timestampIn timestamp entering parking
     * @return ParkingLot describing parking lot details, null if not parked
     */
    public ParkingDetails parkVehicle(final VehicleType vehicleType, final String vehicleNumber, final LocalDateTime timestampIn) {
        if(vehicleType == null) {
            throw new InvalidParameterException("Vehicle type is required");
        }
        if(vehicleNumber == null) {
            throw new InvalidParameterException("License plate is required");
        }
        if(timestampIn == null) {
            throw new InvalidParameterException("Time in is required");
        }

        // get list of parked vehicles
        final List<ParkedVehicleEntity> parkedVehicleList = parkedVehicleRepository.getParkedVehicleByVehicleType(vehicleType);
        // set containing set of parked vehicle numbers
        final Set<String> parkedVehicleNumbers = parkedVehicleList.stream().map(ParkedVehicleEntity::getVehicleNumber)
                .collect(Collectors.toSet());
        // check if vehicle is already parked
        if(parkedVehicleNumbers.contains(vehicleNumber)) {
            throw new VehicleAlreadyParkedException(vehicleType, vehicleNumber);
        }

        // get list of parking lots for vehicle type
        final List<ParkingLotEntity> parkingLotList = parkingLotRepository.getParkingLotByVehicleTypeOrderByLotNumber(
                vehicleType);
        // set containing lot numbers that are already occupied
        final Set<Integer> parkedLotNumbers = parkedVehicleList.stream().map(ParkedVehicleEntity::getLotNumber)
                .collect(Collectors.toSet());
        // find the first available parking lot
        ParkingLotEntity availableLot = null;
        for(ParkingLotEntity parkingLot : parkingLotList) {
            if(parkedLotNumbers.contains(parkingLot.getLotNumber())) {
                // parking lot is occupied
                continue;
            }
            availableLot = parkingLot;
            break;
        }

        if(availableLot == null) {
            return null;
        }
        // saved parked vehicle details
        final ParkedVehicleEntity parkedVehicleEntity = parkedVehicleRepository.save(ParkedVehicleEntity.builder()
                .vehicleType(vehicleType)
                .vehicleNumber(vehicleNumber)
                .lotNumber(availableLot.getLotNumber())
                .timeIn(timestampIn)
                .build());

        // create service bean to return details
        return ParkingDetails.builder()
                .vehicleType(parkedVehicleEntity.getVehicleType())
                .vehicleNumber(parkedVehicleEntity.getVehicleNumber())
                .label(MessageFormat.format(PARKING_LOT_LABEL, parkedVehicleEntity.getVehicleType().getLabel(),
                        parkedVehicleEntity.getLotNumber()))
                .timeIn(parkedVehicleEntity.getTimeIn())
                .build();
    }

    /**
     * Remove vehicle.
     * @param vehicleNumber license plate
     * @param timestampOut timestamp exiting parking
     * @return ParkingLot describing parking lot details
     */
    public ParkingDetails removeVehicle(final String vehicleNumber, final LocalDateTime timestampOut) {
        if(vehicleNumber == null) {
            throw new InvalidParameterException("Vehicle number is required");
        }
        if(timestampOut == null) {
            throw new InvalidParameterException("Time out is required");
        }
        final ParkedVehicleEntity parkedVehicleEntity = parkedVehicleRepository.findParkedVehicleByVehicleNumber(vehicleNumber);
        if(parkedVehicleEntity == null) {
            // not found = not parked
            throw new VehicleNotParkedException(vehicleNumber);
        }
        if(parkedVehicleEntity.getTimeIn().compareTo(timestampOut) > 0) {
            // time out is before time in
            throw new TimeOutBeforeTimeInException();
        }
//        // update time out
//        parkedVehicleEntity.setTimeOut(timestampOut);
//        parkedVehicleRepository.save(parkedVehicleEntity);

        // create service bean to return details
        return ParkingDetails.builder()
                .vehicleType(parkedVehicleEntity.getVehicleType())
                .vehicleNumber(parkedVehicleEntity.getVehicleNumber())
                .label(MessageFormat.format(PARKING_LOT_LABEL, parkedVehicleEntity.getVehicleType().getLabel(),
                        parkedVehicleEntity.getLotNumber()))
                .timeIn(parkedVehicleEntity.getTimeIn())
                .timeOut(parkedVehicleEntity.getTimeOut())
                .build();
    }
}
