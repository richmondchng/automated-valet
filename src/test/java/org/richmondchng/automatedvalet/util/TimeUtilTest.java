package org.richmondchng.automatedvalet.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test TimeUtil.
 *
 * @author richmondchng
 */
class TimeUtilTest {

    /**
     * Test calculateHours.
     *
     * Same time, return 0.
     */
    @Test
    void calculateHours_sameTime_returnZero() {
        final LocalDateTime time1 = LocalDateTime.of(2021, 10, 1, 10, 40, 1);
        final LocalDateTime time2 = LocalDateTime.of(2021, 10, 1, 10, 40, 1);
        assertEquals(0, TimeUtil.calculateHours(time1, time2));
    }

    /**
     * Test calculateHours.
     *
     * Only hours has changed.
     */
    @Test
    void calculateHours_hoursChanged_returnCalculatedHours() {
        final LocalDateTime time1 = LocalDateTime.of(2021, 10, 1, 10, 40, 1);
        final LocalDateTime time2 = LocalDateTime.of(2021, 10, 1, 12, 40, 1);
        assertEquals(2, TimeUtil.calculateHours(time1, time2));
    }

    /**
     * Test calculateHours.
     *
     * Test round up to hour.
     */
    @Test
    void calculateHours_roundUpToNextHour_returnCalculatedHours() {
        final LocalDateTime time1 = LocalDateTime.of(2021, 10, 1, 10, 40, 1);
        final LocalDateTime time2 = LocalDateTime.of(2021, 10, 1, 12, 40, 0);
        // 1 hour + 59min59sec
        assertEquals(2, TimeUtil.calculateHours(time1, time2));
    }
}