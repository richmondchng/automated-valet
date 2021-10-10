package org.richmondchng.automatedvalet.file;

import org.richmondchng.automatedvalet.dto.instruction.ValetAction;
import org.richmondchng.automatedvalet.dto.instruction.ValetInstruction;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;
import org.richmondchng.automatedvalet.util.TimeUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Read file, and translate its content into Valet instructions.
 *
 * @author richmondchng
 */
public final class FileInstructionReader {

    /**
     * Read file and build instructions from file content.
     * @param filePath file path
     * @return FileInstructionsDTO
     * @throws FileNotFoundException
     */
    public static FileInstructionsDTO readInstructions(final String filePath) throws FileNotFoundException {
        final File file = new File(filePath);
        if(!file.isFile()) {
            throw new RuntimeException("File is not valid: " + filePath);
        }
        int numberOfCarLots = 0;
        int numberOfMotorcycleLots = 0;
        final List<ValetInstruction> instructionList = new LinkedList<>();
        try (final Scanner scanner = new Scanner(file)) {
            if(!scanner.hasNextLine()) {
                throw new RuntimeException("File is empty: " + filePath);
            }
            final String config = scanner.nextLine();
            final String[] numberOfLots = config.trim().split(" ");
            if(numberOfLots == null || numberOfLots.length != 2) {
                throw new RuntimeException("First line should contain 2 integer values");
            }
            numberOfCarLots = Integer.valueOf(numberOfLots[0]);
            numberOfMotorcycleLots = Integer.valueOf(numberOfLots[1]);

            while(scanner.hasNextLine()) {
                instructionList.add(buildInstruction(scanner.nextLine()));
            }
        }
        final Map<VehicleType, Integer> numberOfLots = new HashMap<>();
        numberOfLots.put(VehicleType.CAR, numberOfCarLots);
        numberOfLots.put(VehicleType.MOTORCYCLE, numberOfMotorcycleLots);
        return new FileInstructionsDTO(numberOfLots, instructionList);
    }

    /**
     * Build instruction from string.
     * @param line instruction line
     * @return ValetInstruction
     */
    private static ValetInstruction buildInstruction(final String line) {
        final String[] instructionParts = line.trim().split(" ");
        if(instructionParts == null || instructionParts.length < 1) {
            // at least 1 item
            throw new RuntimeException("Instruction is a blank line");
        }
        // this will throw exception if instruction is not defined in enum
        final ValetAction action = ValetAction.getValetAction(instructionParts[0]);
        ValetInstruction instruction = null;
        switch(action) {
            case ENTER:
                if(instructionParts.length != 4) {
                    throw new RuntimeException("Enter instruction should contain 4 parts");
                }
                instruction = ValetInstruction.builder()
                        .action(action)
                        .vehicleType(VehicleType.getVehicleType(instructionParts[1]))
                        .licensePlate(instructionParts[2])
                        .timestamp(TimeUtil.convertSecondsToLocalDateTime(Long.valueOf(instructionParts[3])))
                        .build();
                break;
            case EXIT:
                if(instructionParts.length != 3) {
                    throw new RuntimeException("Exit instruction should contain 3 parts");
                }
                instruction = ValetInstruction.builder()
                        .action(action)
                        .licensePlate(instructionParts[1])
                        .timestamp(TimeUtil.convertSecondsToLocalDateTime(Long.valueOf(instructionParts[2])))
                        .build();
                break;
        }
        if(instruction == null) {
            throw new RuntimeException("Unable to parse instruction: " + line);
        }
        return instruction;
    }
}
