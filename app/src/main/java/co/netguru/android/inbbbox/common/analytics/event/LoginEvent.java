package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginEvent extends ContentEvent {

    public LoginEvent(String method) {
        super(FirebaseAnalytics.Event.SIGN_UP, method);
    }
}
