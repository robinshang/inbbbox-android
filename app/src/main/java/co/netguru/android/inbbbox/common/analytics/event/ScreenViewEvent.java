package co.netguru.android.inbbbox.common.analytics.event;


public class ScreenViewEvent extends ContentEvent {

    private static final String CONTENT_TYPE = "screen";

    public ScreenViewEvent(String screenName) {
        super(CONTENT_TYPE, screenName);
    }
}
