package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.followers.details.adapter.FollowerDetailsAdapter;
import co.netguru.android.inbbbox.view.ShotClickListener;
import dagger.Module;
import dagger.Provides;

@Module
@FragmentScope
public class FollowerDetailsFragmentModule {

    private final ShotClickListener shotClickListener;

    public FollowerDetailsFragmentModule(ShotClickListener shotClickListener) {

        this.shotClickListener = shotClickListener;
    }

    @Provides
    FollowerDetailsAdapter provideShotDetailsAdapter() {
        return new FollowerDetailsAdapter(shotClickListener);
    }

}
