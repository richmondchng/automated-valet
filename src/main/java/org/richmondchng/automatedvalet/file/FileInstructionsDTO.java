package org.richmondchng.automatedvalet.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.richmondchng.automatedvalet.dto.instruction.ValetInstruction;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class FileInstructionsDTO {
    private final Map<VehicleType, Integer> numberOfLots;
    private final List<ValetInstruction> instructions;
}
