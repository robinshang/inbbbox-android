package co.netguru.android.inbbbox.common.analytics.event;


import com.google.firebase.analytics.FirebaseAnalytics;

public class OnboardingStartEvent extends BaseEvent {

    public OnboardingStartEvent() {
        super(FirebaseAnalytics.Event.TUTORIAL_BEGIN);
    }
}
