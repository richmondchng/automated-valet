package org.richmondchng.automatedvalet.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;

/**
 * Entity representing a parked vehicle.
 * @author richmondchng
 */
@Data
@Getter
@Setter
@Builder
public class ParkedVehicleEntity {
    private VehicleType vehicleType;
    private String vehicleNumber;
    private int lotNumber;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private Integer parkingFee;
}
