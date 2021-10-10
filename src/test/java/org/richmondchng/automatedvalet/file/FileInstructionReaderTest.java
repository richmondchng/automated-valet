package org.richmondchng.automatedvalet.file;

import org.junit.jupiter.api.Test;
import org.richmondchng.automatedvalet.dto.instruction.ValetAction;
import org.richmondchng.automatedvalet.dto.instruction.ValetInstruction;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;

import java.time.LocalDateTime;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test FileInstructionReader.
 */
class FileInstructionReaderTest {

    /**
     * Test readInstructions.
     *
     * File is valid, content is valid.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_validFile_returnInstructions() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data.txt").getPath();

        final FileInstructionsDTO results = FileInstructionReader.readInstructions(filePath);
        assertNotNull(results);
        assertEquals(2, results.getNumberOfLots().size());
        assertEquals(3, results.getNumberOfLots().get(VehicleType.CAR));
        assertEquals(4, results.getNumberOfLots().get(VehicleType.MOTORCYCLE));

        assertEquals(2, results.getInstructions().size());
        final Iterator<ValetInstruction> iterator = results.getInstructions().iterator();

        final ValetInstruction result1 = iterator.next();
        assertEquals(ValetAction.ENTER, result1.getAction());
        assertEquals(VehicleType.MOTORCYCLE, result1.getVehicleType());
        assertEquals("SGX1234A", result1.getLicensePlate());
        assertEquals(LocalDateTime.of(2021, 2, 17, 14, 5, 2), result1.getTimestamp());

        final ValetInstruction result2 = iterator.next();
        assertEquals(ValetAction.EXIT, result2.getAction());
        assertNull(result2.getVehicleType());
        assertEquals("SGX1234A", result2.getLicensePlate());
        assertEquals(LocalDateTime.of(2021, 2, 17, 15, 6, 42), result2.getTimestamp());
    }

    /**
     * Test readInstructions.
     *
     * File path is invalid, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_invalidFilePath_throwException() throws Exception {
        final String filePath = "/data/sample-data-does-not-exists.txt";
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("File is not valid: /data/sample-data-does-not-exists.txt", e.getMessage());
        }
    }

    /**
     * Test readInstructions.
     *
     * Empty file, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_emptyFile_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-empty.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertTrue(e.getMessage().startsWith("File is empty: "));
        }
    }

    /**
     * Test readInstructions.
     *
     * First line contain only 1 value, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_firstLineMissingValue_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-missing-motorcycle.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("First line should contain 2 integer values", e.getMessage());
        }
    }

    /**
     * Test readInstructions.
     *
     * First line contain non numeric value, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_firstLineNonNumericValue_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-not-integer.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(NumberFormatException e) {
            assertEquals("For input string: \"A\"", e.getMessage());
        }
    }

    /**
     * Test readInstructions.
     *
     * Enter instruction missing part, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_enterInstructionIncorrectParts_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-enter-missing-part.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Enter instruction should contain 4 parts", e.getMessage());
        }
    }

    /**
     * Test readInstructions.
     *
     * Enter instruction contain non-numeric timestamp, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_enterNonNumericTimestamp_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-enter-timestamp-not-numeric.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(NumberFormatException e) {
            assertEquals("For input string: \"16AA541902\"", e.getMessage());
        }
    }

    /**
     * Test readInstructions.
     *
     * Exit instruction has more parts, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_exitInstructionIncorrectParts_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-exit-missing-part.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Exit instruction should contain 3 parts", e.getMessage());
        }
    }

    /**
     * Test readInstructions.
     *
     * Exit instruction contain non-numeric timestamp, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_exitNonNumericTimestamp_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-exit-timestamp-not-numeric.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(NumberFormatException e) {
            assertEquals("For input string: \"1613545BB2\"", e.getMessage());
        }
    }

    /**
     * Test readInstructions.
     *
     * Invalid action, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_invalidAction_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-invalid-action.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Not a valid action: Parked", e.getMessage(), e.getClass().getSimpleName());
        }
    }

    /**
     * Test readInstructions.
     *
     * Invalid vehicle type, throw exception.
     *
     * @throws Exception
     */
    @Test
    void readInstructions_invalidVehicleType_throwException() throws Exception {
        final String filePath = this.getClass().getResource("/data/sample-data-invalid-vehicle-type.txt").getPath();
        try {
            FileInstructionReader.readInstructions(filePath);
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Not a valid vehicle type: van", e.getMessage(), e.getClass().getSimpleName());
        }
    }
}