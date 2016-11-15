package co.netguru.android.inbbbox.utils;

import android.content.Context;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.R;

@Singleton
public final class LocalTimeFormatter {

    private static final String DATE_PATTER = "yyyy-MM-dd";
    private static final String TIME_PATTER = "h:mm a";
    private static final long SECONDS_IN_MIN = 60;
    private static final long SEC = 1;
    private static final long SECONDS_IN_HOUR = 60 * 60;
    private static final long SECONDS_IN_DAY = 60 * 60 * 24;
    private static final long HALF_OF_MIN = 30;
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
        return LocalDate.now().format(formatter);
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

    public String getTimeLabel(Context context, LocalDateTime dateTime) {
        String label;
        LocalDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Duration duration = Duration.between(now, dateTime);

        if (duration.getSeconds() < LocalTimeFormatter.SEC) {

            label = context.getString(R.string.about_sec_ago);

        } else if (duration.getSeconds() > LocalTimeFormatter.SEC
                && duration.getSeconds() < LocalTimeFormatter.HALF_OF_MIN) {

            label = context.getString(R.string.few_sec_ago);

        } else if (duration.getSeconds() < LocalTimeFormatter.SECONDS_IN_MIN) {

            label = context.getString(R.string.about_minute_ago);

        } else if (duration.getSeconds() > LocalTimeFormatter.SECONDS_IN_MIN
                && duration.getSeconds() < LocalTimeFormatter.SECONDS_IN_HOUR) {

            label = context.getString(R.string.few_minutes_ago);

        } else if (duration.getSeconds() < LocalTimeFormatter.SECONDS_IN_HOUR) {

            label = context.getString(R.string.about_hour_ago);

        } else if (duration.getSeconds() > LocalTimeFormatter.SECONDS_IN_HOUR &&
                duration.getSeconds() < LocalTimeFormatter.SECONDS_IN_DAY) {

            label = context.getString(R.string.few_hours_ago);

        } else if (duration.getSeconds() > LocalTimeFormatter.SECONDS_IN_DAY &&
                duration.getSeconds() < LocalTimeFormatter.SECONDS_IN_DAY * 2) {

            label = context.getString(R.string.yesterday);

        } else {

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern(DATE_PATTER + " " + TIME_PATTER)
                    .withZone(ZoneId.systemDefault());
            label = dateTime.format(formatter);
        }
        return label;
    }
}
