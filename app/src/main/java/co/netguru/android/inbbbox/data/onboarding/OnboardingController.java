package co.netguru.android.inbbbox.data.onboarding;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;

public interface OnboardingController {
    Single<Boolean> isOnboardingPassed();
    void setOnboardingPassed(boolean passed);
}
