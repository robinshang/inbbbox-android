package co.netguru.android.inbbbox.common.analytics.event;


public class RemainingRequestsEvent extends ContentEvent {

    private static final String CONTENT_TYPE = "remaining requests";

    public RemainingRequestsEvent(int requestsRemaining) {
        super(CONTENT_TYPE, Integer.toString(requestsRemaining));
    }
}
