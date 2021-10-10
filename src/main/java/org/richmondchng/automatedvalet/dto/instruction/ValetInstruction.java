package org.richmondchng.automatedvalet.dto.instruction;

import lombok.Builder;
import lombok.Getter;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;

@Getter
@Builder
public class ValetInstruction {
    private final Action action;
    private VehicleType vehicleType;
    private final String licensePlate;
    private final LocalDateTime timestamp;
}
