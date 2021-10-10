package org.richmondchng.automatedvalet.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Utility class to calculate time.
 *
 * @author richmondchng
 */
public final class TimeUtil {

    /**
     * Calculate number of hours between 2 timestamp.
     * @param time1 timestamp 1
     * @param time2 timestamp 2
     * @return number of hours
     */
    public static long calculateHours(final LocalDateTime time1, final LocalDateTime time2) {
        final long secs = time1.until(time2, ChronoUnit.SECONDS);
        long hours = secs / 60 / 60;
        long additionalSec = secs - (hours * 60 * 60);
        if(additionalSec > 0) {
            hours = hours + 1;
        }
        return hours;
    }

    /**
     * Convert ms since epoch to timestamp
     * @param seconds seconds
     * @return LocalDateTime
     */
    public static LocalDateTime convertSecondsToLocalDateTime(final long seconds) {
        // skip nanoseconds
        // default to SG timezone of +8 hours
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, 0), ZoneOffset.ofHours(8));
    }
}
