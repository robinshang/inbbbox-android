package co.netguru.android.inbbbox.feature.follower;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.follower.FollowersFragment;
import co.netguru.android.inbbbox.feature.follower.FollowersPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface FollowersFragmentComponent {

    void inject(FollowersFragment fragment);

    FollowersPresenter getPresenter();
}
