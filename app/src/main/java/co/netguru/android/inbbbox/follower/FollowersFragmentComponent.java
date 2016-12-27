package co.netguru.android.inbbbox.follower;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.follower.FollowersFragment;
import co.netguru.android.inbbbox.follower.FollowersPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface FollowersFragmentComponent {

    void inject(FollowersFragment fragment);

    FollowersPresenter getPresenter();
}
