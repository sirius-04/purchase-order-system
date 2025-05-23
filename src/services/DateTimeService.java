package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String getCurrentDate() {
        return LocalDateTime.now().format(DATE_FORMAT);
    }

    public static String getCurrentTime() {
        return LocalDateTime.now().format(TIME_FORMAT);
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
