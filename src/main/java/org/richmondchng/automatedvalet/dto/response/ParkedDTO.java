package org.richmondchng.automatedvalet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO describing parking lot status.
 *
 * @author richmondchng
 */
@Getter
@Setter
@AllArgsConstructor
public class ParkedDTO {
    private boolean accepted;
    private String lotNumber;
}
