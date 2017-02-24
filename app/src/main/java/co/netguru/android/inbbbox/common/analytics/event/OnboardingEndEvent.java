package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class OnboardingEndEvent extends BaseEvent {

    public OnboardingEndEvent() {
        super(FirebaseAnalytics.Event.TUTORIAL_COMPLETE);
    }
}
