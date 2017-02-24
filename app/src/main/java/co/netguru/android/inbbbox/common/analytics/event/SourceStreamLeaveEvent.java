package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class SourceStreamLeaveEvent extends BaseEvent {

    private static final String EVENT_NAME = "leave_source_stream";

    public SourceStreamLeaveEvent(String groupId) {
        super(EVENT_NAME);
        getParams().putString(FirebaseAnalytics.Param.GROUP_ID, groupId);
    }
}
