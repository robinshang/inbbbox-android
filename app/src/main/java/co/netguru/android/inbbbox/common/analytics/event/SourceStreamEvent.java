package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class SourceStreamEvent extends BaseEvent {

    public SourceStreamEvent(String groupId) {
        super(FirebaseAnalytics.Event.JOIN_GROUP);
        getParams().putString(FirebaseAnalytics.Param.GROUP_ID, groupId);
    }
}
