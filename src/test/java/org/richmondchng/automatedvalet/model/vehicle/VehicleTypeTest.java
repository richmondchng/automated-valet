package org.richmondchng.automatedvalet.model.vehicle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test VehicleType.
 *
 * @author richmondchng
 */
class VehicleTypeTest {

    /**
     * Test getVehicleType.
     *
     * Valid name, return enum object.
     */
    @Test
    void getVehicleType_valid_returnEnum() {
        final VehicleType result = VehicleType.getVehicleType("car");
        assertEquals(VehicleType.CAR, result);
    }

    /**
     * Test getVehicleType.
     *
     * Invalid name, throw exception
     */
    @Test
    void getVehicleType_invalid_throwException() {
        try {
            VehicleType.getVehicleType("bicycle");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Not a valid vehicle type: bicycle", e.getMessage());
        }
    }
}