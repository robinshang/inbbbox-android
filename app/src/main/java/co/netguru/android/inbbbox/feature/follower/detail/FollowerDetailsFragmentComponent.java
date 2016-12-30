package co.netguru.android.inbbbox.feature.follower.detail;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface FollowerDetailsFragmentComponent {

    void inject(FollowerDetailsFragment fragment);

    FollowerDetailsPresenter getPresenter();
}
