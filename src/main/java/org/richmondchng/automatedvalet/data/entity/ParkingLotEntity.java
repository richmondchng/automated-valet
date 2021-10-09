package org.richmondchng.automatedvalet.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

/**
 * Entity representing one parking lot.
 *
 * @author richmondchng
 */
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ParkingLotEntity {
    private VehicleType vehicleType;
    private int lotNumber;
}
