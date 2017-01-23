package co.netguru.android.inbbbox.feature.onboarding;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface OnboardingComponent {
    void inject(OnboardingFragment fragment);

    OnboardingPresenter getPresenter();
}
