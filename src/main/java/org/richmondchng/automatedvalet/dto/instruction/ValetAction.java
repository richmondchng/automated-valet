package org.richmondchng.automatedvalet.dto.instruction;

import java.security.InvalidParameterException;

/**
 * Enumeration of actions available.
 *
 * @author richmondchng
 */
public enum ValetAction {
    ENTER,
    EXIT;

    /**
     * Get action by name.
     * @param name name
     * @return ValetAction
     */
    public static ValetAction getValetAction(final String name) {
        if(name == null) {
            throw new InvalidParameterException("Input value is null");
        }
        ValetAction action = null;
        for(ValetAction va : values()) {
            if(va.name().equalsIgnoreCase(name)) {
                action = va;
                break;
            }
        }
        if(action == null) {
            throw new IllegalArgumentException("Not a valid action: " + name);
        }
        return action;
    }
}
