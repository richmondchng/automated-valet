package org.richmondchng.automatedvalet.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

/**
 * Entity representing parking charges for vehicle type.
 *
 * @author richmondchng
 */
@Data
@Builder
@Getter
@Setter
public class ParkingFeeEntity {
    private VehicleType vehicleType;
    private int parkingFeePerHour;
}
