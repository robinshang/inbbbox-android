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
    private static final long MINUTE_IN_SEC = 60;
    private static final long SEC = 1;
    private static final long HOUR_IN_SEC = 3600;
    private static final long DAY_IN_SEC = 86400;
    private static final long HALF_DAY_IN_SEC = DAY_IN_SEC / 2;
    private static final long HALF_OF_MIN = 30;
    private static final long HALF_MINUTE_IN_SEC = 30;
    private static final long HALF_HOUR_IN_SEC = HOUR_IN_SEC / 2;
    private static final java.lang.String SHOT_DETAILS_FORMAT = "MMM dd, yyyy";
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

        if (duration.getSeconds() <= LocalTimeFormatter.SEC) {

            label = context.getString(R.string.about_sec_ago);

        } else if (duration.getSeconds() >= SEC
                && duration.getSeconds() < HALF_OF_MIN) {

            label = context.getString(R.string.few_sec_ago);

        } else if (duration.getSeconds() > HALF_OF_MIN &&
                duration.getSeconds() <= MINUTE_IN_SEC + HALF_MINUTE_IN_SEC) {

            label = context.getString(R.string.about_minute_ago);

        } else if (duration.getSeconds() > MINUTE_IN_SEC + HALF_MINUTE_IN_SEC
                && duration.getSeconds() <= HALF_HOUR_IN_SEC) {

            label = context.getString(R.string.few_minutes_ago);

        } else if (duration.getSeconds() > HALF_HOUR_IN_SEC
                && duration.getSeconds() <= HOUR_IN_SEC + HALF_HOUR_IN_SEC) {

            label = context.getString(R.string.about_hour_ago);

        } else if (duration.getSeconds() > HOUR_IN_SEC + HALF_HOUR_IN_SEC
                && duration.getSeconds() <= DAY_IN_SEC) {

            label = context.getString(R.string.few_hours_ago);

        } else if (duration.getSeconds() >= DAY_IN_SEC &&
                duration.getSeconds() < DAY_IN_SEC + HALF_DAY_IN_SEC) {

            label = context.getString(R.string.yesterday);

        } else {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern(DATE_PATTER + " " + TIME_PATTER)
                    .withZone(ZoneId.systemDefault());
            label = dateTime.format(formatter);
        }
        return label;
    }

    public static String getShotDetailsDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(SHOT_DETAILS_FORMAT)
                .withZone(ZoneId.systemDefault());
        return date.format(formatter);
    }
}
