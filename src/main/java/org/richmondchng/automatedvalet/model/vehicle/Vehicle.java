package org.richmondchng.automatedvalet.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Vehicle {
    private final VehicleType vehicleType;
    private final String vehicleNumber;
}
