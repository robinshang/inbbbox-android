package co.netguru.android.inbbbox.common.analytics.event;

import com.google.firebase.analytics.FirebaseAnalytics;

public class UserInteractionEvent extends BaseEvent {

    private static final String EVENT_NAME = "user_interaction";

    public UserInteractionEvent(String itemName) {
        super(EVENT_NAME);
        getParams().putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
    }
}
