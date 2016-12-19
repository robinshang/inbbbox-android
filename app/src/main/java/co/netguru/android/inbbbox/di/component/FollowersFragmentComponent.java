package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.di.module.FollowersFragmentModule;
import co.netguru.android.inbbbox.feature.followers.FollowersFragment;
import co.netguru.android.inbbbox.feature.followers.FollowersPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = FollowersFragmentModule.class)
public interface FollowersFragmentComponent {

    void inject(FollowersFragment fragment);

    FollowersPresenter getPresenter();
}
