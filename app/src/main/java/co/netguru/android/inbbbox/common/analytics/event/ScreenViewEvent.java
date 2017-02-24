package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class ScreenViewEvent extends BaseEvent {

    private static final String CONTENT_TYPE = "screen";

    public ScreenViewEvent(String screenName) {
        super(FirebaseAnalytics.Event.SELECT_CONTENT);
        getParams().putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE);
        getParams().putString(FirebaseAnalytics.Param.ITEM_ID, screenName);
    }
}
