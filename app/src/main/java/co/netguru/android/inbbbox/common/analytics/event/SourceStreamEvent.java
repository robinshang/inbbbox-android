package co.netguru.android.inbbbox.common.analytics.event;


public class SourceStreamEvent extends ContentEvent {

    private static final String CONTENT_TYPE = "source stream";

    public SourceStreamEvent(String groupId, boolean joined) {
        super(CONTENT_TYPE, String.format("%s %b", groupId, joined));
    }
}
