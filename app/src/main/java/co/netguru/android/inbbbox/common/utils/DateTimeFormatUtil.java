package co.netguru.android.inbbbox.common.utils;

import android.content.Context;
import android.text.format.DateFormat;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import co.netguru.android.inbbbox.R;

public final class DateTimeFormatUtil {

    private final Context context;
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TWELVE_HOUR_CLOCK_PATTERN = "h:mm a";
    private static final String TWENTYFOUR_HOUR_CLOCK_PATTERN = "H:mm";
    private static final String MONTH_SHORT_DAY_AND_YEAR_FORMAT = "MMM dd, yyyy";

    private static final long MINUTE_IN_SEC = 60;
    private static final long SEC = 1;
    private static final long HOUR_IN_SEC = 3600;
    private static final long DAY_IN_SEC = 86400;
    private static final long HALF_DAY_IN_SEC = DAY_IN_SEC / 2;
    private static final long HALF_OF_MIN = 30;
    private static final long HALF_MINUTE_IN_SEC = 30;
    private static final long HALF_HOUR_IN_SEC = HOUR_IN_SEC / 2;
    private static final String SHOT_DETAILS_FORMAT = "MMM dd, yyyy";

    private static final DateTimeFormatter TWELVE_HOUR_CLOCK_FORMATTER = DateTimeFormatter.ofPattern(TWELVE_HOUR_CLOCK_PATTERN)
            .withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter TWENTYFOUR_HOUR_CLOCK_FORMATTER = DateTimeFormatter
            .ofPattern(TWENTYFOUR_HOUR_CLOCK_PATTERN)
            .withZone(ZoneId.systemDefault());

    public DateTimeFormatUtil(Context context) {
        this.context = context;
    }

    public String getFormattedTime(int hour, int minute) {
        return LocalTime.of(hour, minute).format(DateFormat.is24HourFormat(context) ?
                TWENTYFOUR_HOUR_CLOCK_FORMATTER : TWELVE_HOUR_CLOCK_FORMATTER);
    }

    public static String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return ZonedDateTime.now().format(formatter);
    }

    public static long getSecondsFromTime(int hour, int minute) {
        return ZonedDateTime.of(getDate(hour, minute), getLocalTime(hour, minute), ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static LocalDate getDate(int hour, int minute) {
        if (LocalTime.of(hour, minute).isAfter(LocalTime.now())) {
            return LocalDate.now();
        }
        return LocalDate.now().plusDays(1);
    }

    private static LocalTime getLocalTime(int hour, int minute) {
        return LocalTime.of(hour, minute, 0);
    }

    public static String getTimeLabel(Context context, ZonedDateTime dateTime) {
        String label;
        ZonedDateTime now = ZonedDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        if (duration.getSeconds() <= DateTimeFormatUtil.SEC) {

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
                    .ofPattern(DATE_PATTERN + " " + TWELVE_HOUR_CLOCK_PATTERN)
                    .withZone(ZoneId.systemDefault());
            label = dateTime.format(formatter);
        }
        return label;
    }

    public static String getMonthShortDayAndYearFormattedDate(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(MONTH_SHORT_DAY_AND_YEAR_FORMAT));
    }

    public static String getShotDetailsDate(ZonedDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(SHOT_DETAILS_FORMAT);
        return date.format(formatter);
    }
}
