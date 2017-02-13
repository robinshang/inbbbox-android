package co.netguru.android.inbbbox.feature.onboarding;

import android.content.Context;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.onboarding.recycler.OnboardingLinearLayoutManager;
import co.netguru.android.inbbbox.feature.onboarding.recycler.OnboardingShotsAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class OnboardingModule {

    OnboardingShotSwipeListener shotSwipeListener;
    Context context;

    public OnboardingModule(OnboardingShotSwipeListener shotSwipeListener, Context context) {
        this.shotSwipeListener = shotSwipeListener;
        this.context = context;
    }

    @Provides
    @FragmentScope
    OnboardingShotsAdapter provideShotsAdapter() {
        return new OnboardingShotsAdapter(shotSwipeListener);
    }

    @Provides
    @FragmentScope
    OnboardingLinearLayoutManager provideLayoutManager() {
        return new OnboardingLinearLayoutManager(context);
    }

}
