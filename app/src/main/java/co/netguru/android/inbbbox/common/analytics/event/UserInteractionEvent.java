package co.netguru.android.inbbbox.common.analytics.event;

public class UserInteractionEvent extends ContentEvent {

    private static final String CONTENT_TYPE = "user_interaction";

    public UserInteractionEvent(String itemName) {
        super(CONTENT_TYPE, itemName);
    }
}
