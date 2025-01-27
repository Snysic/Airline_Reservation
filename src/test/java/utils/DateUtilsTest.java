package utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    public void testParseStringToLocalDateTime() {
        String validDateString = "2025-01-26 15:30:45";
        LocalDateTime expectedDateTime = LocalDateTime.of(2025, 1, 26, 15, 30, 45);

        assertEquals(expectedDateTime, DateUtils.parseStringToLocalDateTime(validDateString));

        String invalidDateString = "invalid-date";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
                DateUtils.parseStringToLocalDateTime(invalidDateString));

        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    public void testFormatLocalDateTimeToString() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 26, 15, 30, 45);
        String expectedDateString = "2025-01-26 15:30:45";

        assertEquals(expectedDateString, DateUtils.formatLocalDateTimeToString(dateTime));
    }

    @Test
    public void testIsFutureDate() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertTrue(DateUtils.isFutureDate(futureDate));

        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertFalse(DateUtils.isFutureDate(pastDate));
    }

    @Test
    public void testIsPastDate() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertTrue(DateUtils.isPastDate(pastDate));

        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertFalse(DateUtils.isPastDate(futureDate));
    }

    @Test
    public void testIsWithinRange() {
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 25, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 27, 0, 0, 0);

        LocalDateTime withinRangeDate = LocalDateTime.of(2025, 1, 26, 12, 0, 0);
        assertTrue(DateUtils.isWithinRange(withinRangeDate, startDate, endDate));

        LocalDateTime outsideRangeDate = LocalDateTime.of(2025, 1, 28, 12, 0, 0);
        assertFalse(DateUtils.isWithinRange(outsideRangeDate, startDate, endDate));
    }
}

