package airlinereservation.project.Airlinereservation.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
    }

    public static LocalDateTime parseStringToLocalDateTime(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: " + DEFAULT_DATE_TIME_FORMAT);
        }
    }

    public static String formatLocalDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }

    public static boolean isFutureDate(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean isPastDate(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isWithinRange(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        return !dateTime.isBefore(start) && !dateTime.isAfter(end);
    }
}
