package org.richmondchng.automatedvalet;

import org.richmondchng.automatedvalet.config.ContextConfig;
import org.richmondchng.automatedvalet.config.ParkingLotConfiguration;
import org.richmondchng.automatedvalet.controller.ParkingValetController;
import org.richmondchng.automatedvalet.dto.instruction.ValetInstruction;
import org.richmondchng.automatedvalet.dto.response.ParkedDTO;
import org.richmondchng.automatedvalet.dto.response.ParkingFeeDTO;
import org.richmondchng.automatedvalet.file.FileInstructionReader;
import org.richmondchng.automatedvalet.file.FileInstructionsDTO;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.io.FileNotFoundException;
import java.text.MessageFormat;

/**
 * Main class.
 * @author richmondchng
 */
public class AutomatedValet {

    /**
     * Main class. This should take 1 parameter which is the file path.
     * @param args should include file path to data.
     * @throws FileNotFoundException if file is invalid
     */
    public static void main(final String[] args) throws FileNotFoundException {
        if(args == null || args.length != 1) {
            throw new RuntimeException("Missing file parameter");
        }
        final String filePath = args[0];
        // read file into instructions
        final FileInstructionsDTO instructions = FileInstructionReader.readInstructions(filePath);

        // build context
        final AutomatedValet automatedValet = new AutomatedValet(
                instructions.getNumberOfLots().get(VehicleType.CAR), 2,
                instructions.getNumberOfLots().get(VehicleType.MOTORCYCLE), 1);
        for(ValetInstruction instruction : instructions.getInstructions()) {
            switch (instruction.getAction()) {
                case ENTER:
                    System.out.println(automatedValet.enterParking(instruction));
                    break;
                case EXIT:
                    System.out.println(automatedValet.exitParking(instruction));
                    break;
            }
        }
    }

    private static final String VEHICLE_PARKED = "Accept {0}";
    private static final String VEHICLE_NOT_PARKED = "Reject";
    private static final String VEHICLE_EXIT = "{0} {1}";   // 0 = label, 1 = fee per hour
    private final ParkingValetController parkingValetController;

    /**
     * Constructor.
     * @param numCarLots number of lots for car
     * @param feePerHourCar fee per hour for car
     * @param numMotorcycleLots number of lots for motorcycle
     * @param feePerHourMotorcycle fee per hour for motorcycle
     */
    private AutomatedValet(final int numCarLots, final int feePerHourCar, final int numMotorcycleLots, final int feePerHourMotorcycle) {
        this(new ParkingLotConfiguration[]{
                new ParkingLotConfiguration(VehicleType.CAR, numCarLots, feePerHourCar),
                new ParkingLotConfiguration(VehicleType.MOTORCYCLE, numMotorcycleLots, feePerHourMotorcycle)
        });
    }

    /**
     * Constructor. Insert parking configurations.
     * @param configurations array of parking lot configuration
     */
    AutomatedValet(final ParkingLotConfiguration[] configurations) {
        final ContextConfig contextConfig = new ContextConfig(configurations);
        // get controller
        parkingValetController = contextConfig.parkingValetController();
    }

    /**
     * Accept instruction to park vehicle.
     * @param valetInstruction instruction
     * @return output string
     */
    public String enterParking(final ValetInstruction valetInstruction) {
        final ParkedDTO result = parkingValetController.enterParking(valetInstruction.getVehicleType(),
                valetInstruction.getLicensePlate(), valetInstruction.getTimestamp());
        if(result.isAccepted()) {
            // build output string
            return MessageFormat.format(VEHICLE_PARKED, result.getLotNumber());
        }
        // reject
        return VEHICLE_NOT_PARKED;
    }

    /**
     * Accept instruction to exit vehicle
     * @param valetInstruction instruction
     * @return output string
     */
    public String exitParking(final ValetInstruction valetInstruction) {
        final ParkingFeeDTO result = parkingValetController.exitParking(valetInstruction.getLicensePlate(),
                valetInstruction.getTimestamp());
        // build output string
        return MessageFormat.format(VEHICLE_EXIT, result.getLabel(), result.getParkingFee());
    }
}
