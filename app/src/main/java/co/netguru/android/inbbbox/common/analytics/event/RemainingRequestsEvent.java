package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class RemainingRequestsEvent extends BaseEvent {

    private static final String CHARACTER = "remaining requests";

    public RemainingRequestsEvent(int requestsRemaining) {
        super(FirebaseAnalytics.Event.POST_SCORE);
        getParams().putString(FirebaseAnalytics.Param.CHARACTER, CHARACTER);
        getParams().putLong(FirebaseAnalytics.Param.SCORE, requestsRemaining);
    }
}
