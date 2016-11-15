package co.netguru.android.inbbbox.utils;


import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public final class DateFormatUtil {

    private static final String MONTH_SHORT_DAY_AND_YEAR_FORMAT = "MMM dd, yyyy";

    private DateFormatUtil() {
        throw new AssertionError();
    }

    public static String getMonthShortDayAndYearFormatedDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(MONTH_SHORT_DAY_AND_YEAR_FORMAT));
    }
}
