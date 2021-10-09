package org.richmondchng.automatedvalet.data.storage;

import org.richmondchng.automatedvalet.data.entity.ParkedVehicleEntity;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Data storage representing data storage for ParkedVehicleEntity.
 *
 * This can then be replaced with actual database, or other data storage.
 *
 * @author richmondchng
 */
public class ParkedVehicleDataStorage {

    private final List<ParkedVehicleEntity> parkedVehiclesEntities;

    private static final String ERROR_ID_IS_INVALID = "Id {0} is invalid";

    public ParkedVehicleDataStorage() {
        parkedVehiclesEntities = new LinkedList<>();
    }

    /**
     * Save entity bean.
     * @param parkedVehicleEntity ParkedVehicleEntity
     * @return updated bean
     */
    public ParkedVehicleEntity save(final ParkedVehicleEntity parkedVehicleEntity) {
        if(parkedVehicleEntity == null) {
            throw new InvalidParameterException("ParkedVehicleEntity cannot be null");
        }
        ParkedVehicleEntity data = null;
        if(parkedVehicleEntity.getId() != null) {
            // existing object
            data = parkedVehiclesEntities.stream().filter(b -> b.getId().equals(parkedVehicleEntity.getId()))
                    .findAny().orElse(null);
            if(data == null) {
                throw new InvalidParameterException(MessageFormat.format(ERROR_ID_IS_INVALID, parkedVehicleEntity.getId()));
            }
        } else {
            // new object
            data = ParkedVehicleEntity.builder()
                    .id(UUID.randomUUID())
                    .vehicleType(parkedVehicleEntity.getVehicleType())
                    .vehicleNumber(parkedVehicleEntity.getVehicleNumber())
                    .lotNumber(parkedVehicleEntity.getLotNumber())
                    .build();
            // add to list
            parkedVehiclesEntities.add(data);
        }
        data.setTimeIn(parkedVehicleEntity.getTimeIn());
        data.setTimeOut(parkedVehicleEntity.getTimeOut());
        data.setParkingFee(parkedVehicleEntity.getParkingFee());

        // return a copy of data
        return copy(data);
    }

    /**
     * Get list of parked vehicles by vehicle type.
     * @param vehicleType vehicle type
     * @return unmodifiable list of ParkedVehicleEntity beans
     */
    public List<ParkedVehicleEntity> getParkedVehiclesByVehicleType(final VehicleType vehicleType) {
        if(vehicleType == null) {
            throw new InvalidParameterException("VehicleType cannot be null");
        }
        // return an unmodifiable list
        return parkedVehiclesEntities.stream()
                .filter(b -> vehicleType.equals(b.getVehicleType()) && b.getTimeOut() == null)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Get vehicle by vehicle number.
     * @param vehicleNumber vehicle number
     * @return ParkedVehicleEntity or null
     */
    public ParkedVehicleEntity getParkedVehicleByVehicleNumber(final String vehicleNumber) {
        if(vehicleNumber == null) {
            throw new InvalidParameterException("Vehicle number cannot be null");
        }
        final ParkedVehicleEntity entity = parkedVehiclesEntities.stream()
                .filter(b -> vehicleNumber.equals(b.getVehicleNumber()) && b.getTimeOut() == null)
                .findAny().orElse(null);
        if(entity != null) {
            return copy(entity);
        }
        return null;
    }

    /**
     * Find record by Id.
     * @param id record Id
     * @return ParkedVehicleEntity or null
     */
    public ParkedVehicleEntity getRecordById(final UUID id) {
        if(id == null) {
            throw new InvalidParameterException("Id cannot be null");
        }
        final ParkedVehicleEntity entity = parkedVehiclesEntities.stream()
                .filter(b -> id.equals(b.getId()))
                .findAny().orElse(null);
        if(entity != null) {
            return copy(entity);
        }
        return null;
    }

    /**
     * Make a copy
     * @param src ParkedVehicleEntity
     * @return ParkedVehicleEntity
     */
    private ParkedVehicleEntity copy(final ParkedVehicleEntity src) {
        return ParkedVehicleEntity.builder()
                .id(src.getId())
                .vehicleType(src.getVehicleType())
                .vehicleNumber(src.getVehicleNumber())
                .lotNumber(src.getLotNumber())
                .timeIn(src.getTimeIn())
                .timeOut(src.getTimeOut())
                .parkingFee(src.getParkingFee())
                .build();
    }
}
