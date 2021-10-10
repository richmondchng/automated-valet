package org.richmondchng.automatedvalet.dto.instruction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test ValetAction.
 *
 * @author richmondchng
 */
class ValetActionTest {

    /**
     * Test getValetAction.
     *
     * Valid name, return enum object.
     */
    @Test
    void getValetAction_valid_returnEnum() {
        final ValetAction result = ValetAction.getValetAction("enter");
        assertEquals(ValetAction.ENTER, result);
    }

    /**
     * Test getValetAction.
     *
     * Invalid name, throw exception
     */
    @Test
    void getValetAction_invalid_throwException() {
        try {
            ValetAction.getValetAction("parked");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Not a valid action: parked", e.getMessage());
        }
    }
}