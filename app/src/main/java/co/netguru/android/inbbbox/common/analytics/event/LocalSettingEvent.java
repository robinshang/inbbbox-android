package co.netguru.android.inbbbox.common.analytics.event;


public class LocalSettingEvent extends ContentEvent {

    private static final String CONTENT_TYPE = "local_setting";

    public LocalSettingEvent(String settingName, boolean enabled) {
        super(CONTENT_TYPE, String.format("%s %b", settingName, enabled));
    }
}
