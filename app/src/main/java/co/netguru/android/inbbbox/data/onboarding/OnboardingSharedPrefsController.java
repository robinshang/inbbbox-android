package co.netguru.android.inbbbox.data.onboarding;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class OnboardingSharedPrefsController implements OnboardingController {

    public static final String KEY_PASSED = "onboarding:passed";

    private final SharedPreferences sharedPreferences;

    @Inject
    public OnboardingSharedPrefsController(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Single<Boolean> isOnboardingPassed() {
        return Single.just(sharedPreferences.getBoolean(KEY_PASSED, false));
    }

    @Override
    public void setOnboardingPassed(boolean passed) {
        sharedPreferences.edit().putBoolean(KEY_PASSED, passed).apply();
    }
}
