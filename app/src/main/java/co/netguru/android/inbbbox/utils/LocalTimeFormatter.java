package co.netguru.android.inbbbox.utils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class LocalTimeFormatter {

    private static final String DATE_PATTER = "YYYY-MM-DD";
    private static final String TIME_PATTER = "h:mm a";
    private final DateTimeFormatter dateTimeFormatter;

    @Inject
    LocalTimeFormatter() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTER).withZone(ZoneId.systemDefault());
    }

    public String getFormattedTime(int hour, int minute) {
        return LocalTime.of(hour, minute).format(dateTimeFormatter);
    }

    public String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTER).withZone(ZoneId.systemDefault());
        return LocalTime.now().format(formatter);
    }

    public long getSecondsFromTime(int hour, int minute) {
        return LocalDateTime.of(getDate(hour, minute), getLocalTime(hour, minute)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private LocalDate getDate(int hour, int minute) {
        if (LocalTime.of(hour, minute).isAfter(LocalTime.now())) {
            return LocalDate.now();
        }
        return LocalDate.now().plusDays(1);
    }

    private LocalTime getLocalTime(int hour, int minute) {
        return LocalTime.of(hour, minute, 0);
    }
}
