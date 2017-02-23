package co.netguru.android.inbbbox.common.analytics.event;

import com.google.firebase.analytics.FirebaseAnalytics;

public class ButtonPressEvent extends BaseEvent {

    private static final String EVENT_NAME = "user_interaction";

    public ButtonPressEvent(String buttonName) {
        super(EVENT_NAME);
        getParams().putString(FirebaseAnalytics.Param.ITEM_NAME, buttonName);
    }
}
