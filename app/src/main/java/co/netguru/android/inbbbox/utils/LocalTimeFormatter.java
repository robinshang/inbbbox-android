package co.netguru.android.inbbbox.utils;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.di.scope.ActivityScope;

@ActivityScope
public final class LocalTimeFormatter {

    private static final String TIME_PATTER = "h:mm a";

    @Inject
    LocalTimeFormatter() {
    }

    public int getCurrentHour() {
        return LocalTime.now().getHour();
    }

    public int getCurrentMinute() {
        return LocalTime.now().getMinute();
    }

    public String getFormattedTime(int hour, int minute) {
        return LocalTime.of(hour, minute).format(DateTimeFormatter.ofPattern(TIME_PATTER));
    }

    public String getFormattedCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTER));
    }
}
