package org.richmondchng.automatedvalet.dto.instruction;

import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;

public class ValetInstruction {
    private Action action;
    private VehicleType vehicleType;
    private String licensePlate;
    private LocalDateTime timestamp;
}
