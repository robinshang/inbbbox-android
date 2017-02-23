package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginEvent extends BaseEvent {

    public LoginEvent(String method) {
        super(FirebaseAnalytics.Event.SIGN_UP);
        getParams().putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, method);
    }
}
