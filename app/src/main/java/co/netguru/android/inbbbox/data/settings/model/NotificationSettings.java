package co.netguru.android.inbbbox.data.settings.model;

import org.threeten.bp.LocalTime;

public class NotificationSettings {

    private boolean enabled;
    private int hour;
    private int minute;

    public NotificationSettings() {
        hour = LocalTime.now().getHour();
        minute = LocalTime.now().getMinute();
    }

    public NotificationSettings(boolean enabled, int hour, int minute) {
        this.enabled = enabled;
        this.hour = hour;
        this.minute = minute;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
