package org.richmondchng.automatedvalet;

import org.richmondchng.automatedvalet.config.ContextConfig;
import org.richmondchng.automatedvalet.config.ParkingLotConfiguration;
import org.richmondchng.automatedvalet.controller.ParkingValetController;
import org.richmondchng.automatedvalet.dto.instruction.ValetInstruction;
import org.richmondchng.automatedvalet.dto.response.ParkedDTO;
import org.richmondchng.automatedvalet.dto.response.ParkingFeeDTO;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.text.MessageFormat;

/**
 * Main class.
 * @author richmondchng
 */
public class AutomatedValet {

    public static void main(final String[] args) {
    }

    private static final String VEHICLE_PARKED = "Accept {0}";
    private static final String VEHICLE_NOT_PARKED = "Reject";
    private static final String VEHICLE_EXIT = "{0} {1}";   // 0 = label, 1 = fee
    private final ParkingValetController parkingValetController;

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
            return MessageFormat.format(VEHICLE_PARKED, result.getLotNumber());
        }
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
        return MessageFormat.format(VEHICLE_EXIT, result.getLabel(), result.getParkingFee());
    }
}
