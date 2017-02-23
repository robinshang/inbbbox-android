package co.netguru.android.inbbbox.common.analytics.event;

import com.google.firebase.analytics.FirebaseAnalytics;

public class ShotsSwipeEvent extends BaseEvent {

    private static final String EVENT_NAME = "user_interaction";
    private static final String ITEM_NAME = "shots list swipe";

    public ShotsSwipeEvent(int itemsSwiped) {
        super(EVENT_NAME);
        getParams().putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME);
        getParams().putLong(FirebaseAnalytics.Param.VALUE, itemsSwiped);
    }
}
