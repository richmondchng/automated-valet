package org.richmondchng.automatedvalet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richmondchng.automatedvalet.config.ParkingLotConfiguration;
import org.richmondchng.automatedvalet.dto.instruction.ValetAction;
import org.richmondchng.automatedvalet.dto.instruction.ValetInstruction;
import org.richmondchng.automatedvalet.exception.TimeOutBeforeTimeInException;
import org.richmondchng.automatedvalet.exception.VehicleAlreadyParkedException;
import org.richmondchng.automatedvalet.exception.VehicleNotParkedException;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;
import org.richmondchng.automatedvalet.util.TimeUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integrate full context for testing. Test feature without file loading.
 *
 * @author richmondchng
 */
class AutomatedValetIntegrationTest {

    // test instance
    private AutomatedValet automatedValet;

    @BeforeEach
    void setUp() {
        // 3 car lots, at $2 per hour
        // 4 motorcycle lots, at $1 per hour
        automatedValet = new AutomatedValet(new ParkingLotConfiguration[]{
                new ParkingLotConfiguration(VehicleType.CAR, 3, 2),
                new ParkingLotConfiguration(VehicleType.MOTORCYCLE, 4, 1)
        });
    }

    @AfterEach
    void tearDown() {
        automatedValet = null;
    }

    /**
     * Test instructions without any errors.
     */
    @Test
    void testInstructionsNoError() {
        // Enter motorcycle SGX1234A 1613541902  => Accept MotorcycleLot1
        // Enter car SGF9283P 1613541902         => Accept CarLot1
        // Exit SGX1234A 1613545602              => MotorcycleLot1 2
        // Enter car SGP2937F 1613546029         => Accept CarLot2
        // Enter car SDW2111W 1613549730         => Accept CarLot3
        // Enter car SSD9281L 1613549740         => Reject
        // Exit SDW2111W 1613559745              => CarLot3 6
        assertEquals("Accept MotorcycleLot1",
                automatedValet.enterParking(enter(VehicleType.MOTORCYCLE, "SGX1234A", 1613541902L)));
        assertEquals("Accept CarLot1",
                automatedValet.enterParking(enter(VehicleType.CAR, "SGF9283P", 1613541902L)));
        assertEquals("MotorcycleLot1 2",
                automatedValet.exitParking(exit("SGX1234A", 1613545602L)));
        assertEquals("Accept CarLot2",
                automatedValet.enterParking(enter(VehicleType.CAR, "SGP2937F", 1613546029L)));
        assertEquals("Accept CarLot3",
                automatedValet.enterParking(enter(VehicleType.CAR, "SDW2111W", 1613549730L)));
        assertEquals("Reject",
                automatedValet.enterParking(enter(VehicleType.CAR, "SSD9281L", 1613549740L)));
        assertEquals("CarLot3 6",
                automatedValet.exitParking(exit("SDW2111W", 1613559745L)));
    }

    /**
     * Test instructions with error.
     *
     * Try to exit a vehicle that is not in parking.
     */
    @Test
    void testInstructionsVehicleNotInParking() {
        // Enter motorcycle SGX1234A 1613541902  => Accept MotorcycleLot1
        // Enter car SGF9283P 1613541902         => Accept CarLot1
        // Exit SDW2111W 1613559745              => Exception
        automatedValet.enterParking(enter(VehicleType.MOTORCYCLE, "SGX1234A", 1613541902L));
        automatedValet.enterParking(enter(VehicleType.CAR, "SGF9283P", 1613541902L));
        try {
            automatedValet.exitParking(exit("SDW2111W", 1613559745L));
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof VehicleNotParkedException);
        }
    }

    /**
     * Test instructions with error.
     *
     * Same vehicle entering car park
     */
    @Test
    void testInstructionsDoubleParked() {
        // Enter motorcycle SGX1234A 1613541902  => Accept MotorcycleLot1
        // Enter car SGF9283P 1613541902         => Accept CarLot1
        // Enter motorcycle SGX1234A 1613541902  => Exception
        automatedValet.enterParking(enter(VehicleType.MOTORCYCLE, "SGX1234A", 1613541902L));
        automatedValet.enterParking(enter(VehicleType.CAR, "SGF9283P", 1613541902L));
        try {
            automatedValet.enterParking(enter(VehicleType.MOTORCYCLE, "SGX1234A", 1613541902L));
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof VehicleAlreadyParkedException);
        }
    }

    /**
     * Test instructions with error.
     *
     * Time stamp is incorrect
     */
    @Test
    void testInstructionsIncorrectTimeout() {
        // Enter motorcycle SGX1234A 1613545602  => Accept MotorcycleLot1
        // Exit SGX1234A 1613541902              => MotorcycleLot1 2
        assertEquals("Accept MotorcycleLot1",
                automatedValet.enterParking(enter(VehicleType.MOTORCYCLE, "SGX1234A", 1613545602L)));
        try {
            automatedValet.exitParking(exit("SGX1234A", 1613541902L));
            fail("Expect exception to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e instanceof TimeOutBeforeTimeInException);
        }
    }

    private ValetInstruction enter(final VehicleType vehicleType, final String vehicleNumber, final long seconds) {
        return ValetInstruction.builder()
                .action(ValetAction.ENTER)
                .vehicleType(vehicleType)
                .licensePlate(vehicleNumber)
                .timestamp(TimeUtil.convertSecondsToLocalDateTime(seconds))
                .build();
    }

    private ValetInstruction exit(final String vehicleNumber, final long seconds) {
        return ValetInstruction.builder()
                .action(ValetAction.EXIT)
                .licensePlate(vehicleNumber)
                .timestamp(TimeUtil.convertSecondsToLocalDateTime(seconds))
                .build();
    }
}