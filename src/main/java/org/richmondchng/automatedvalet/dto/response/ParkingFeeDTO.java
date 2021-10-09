package org.richmondchng.automatedvalet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO describing parking fee.
 *
 * @author richmondchng
 */
@Getter
@Setter
@AllArgsConstructor
public class ParkingFeeDTO {
    private String label;
    private int parkingFee;
}
