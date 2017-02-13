package co.netguru.android.inbbbox.data.onboarding;

import rx.Single;

public interface OnboardingController {
    Single<Boolean> isOnboardingPassed();

    void setOnboardingPassed(boolean passed);
}
