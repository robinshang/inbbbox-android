package co.netguru.android.inbbbox.feature.onboarding;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenModule;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = OnboardingModule.class)
public interface OnboardingComponent {
    void inject(OnboardingFragment fragment);

    OnboardingPresenter getPresenter();
}
