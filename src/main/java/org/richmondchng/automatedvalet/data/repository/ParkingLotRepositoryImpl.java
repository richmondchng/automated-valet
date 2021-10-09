package org.richmondchng.automatedvalet.data.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.richmondchng.automatedvalet.data.entity.ParkingLotEntity;
import org.richmondchng.automatedvalet.data.storage.ParkingLotDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation for ParkingLotRepository.
 *
 * This uses "in-memory" Java object data storage.
 *
 * @author richmondchng
 */
@RequiredArgsConstructor
public class ParkingLotRepositoryImpl implements ParkingLotRepository {

    private final ParkingLotDataStorage parkingLotDataStorage;

    @Override
    public List<ParkingLotEntity> finalAllParkingLotsByVehicleTypeOrderByLotNumber(final VehicleType vehicleType) {
        final List<ParkingLotEntity> parkingLotEntityList = parkingLotDataStorage.getByVehicleType(vehicleType);
        if(CollectionUtils.isEmpty(parkingLotEntityList)) {
            return Collections.emptyList();
        }
        return parkingLotEntityList.stream()
                // sort by lot number (natural order ascending)
                .sorted(Comparator.comparing(ParkingLotEntity::getLotNumber))
                // into an unmodifiable list
                .collect(Collectors.toUnmodifiableList());
    }
}
