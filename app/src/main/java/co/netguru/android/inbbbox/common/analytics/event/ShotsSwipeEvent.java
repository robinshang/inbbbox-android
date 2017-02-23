package co.netguru.android.inbbbox.common.analytics.event;

import com.google.firebase.analytics.FirebaseAnalytics;

public class ShotsSwipeEvent extends UserInteractionEvent {

    public ShotsSwipeEvent(String itemName, int itemsSwiped) {
        super(itemName);
        getParams().putLong(FirebaseAnalytics.Param.VALUE, itemsSwiped);
    }
}
