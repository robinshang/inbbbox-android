package co.netguru.android.inbbbox.feature.follower.detail;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsFragment;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface FollowerDetailsFragmentComponent {

    void inject(FollowerDetailsFragment fragment);

    FollowerDetailsPresenter getPresenter();
}
