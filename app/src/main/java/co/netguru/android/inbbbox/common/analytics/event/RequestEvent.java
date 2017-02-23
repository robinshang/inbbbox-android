package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class RequestEvent extends BaseEvent {

    private static final String CURRENCY = "request";

    public RequestEvent(String requestName) {
        super(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY);
        getParams().putString(FirebaseAnalytics.Param.ITEM_NAME, requestName);
        getParams().putString(FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME, CURRENCY);
        getParams().putLong(FirebaseAnalytics.Param.VALUE, 1);
    }
}
