package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.di.module.FollowerDetailsFragmentModule;
import co.netguru.android.inbbbox.feature.followers.details.FollowerDetailsFragment;
import co.netguru.android.inbbbox.feature.followers.details.FollowerDetailsPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = FollowerDetailsFragmentModule.class)
public interface FollowerDetailsFragmentComponent {

    void inject(FollowerDetailsFragment fragment);

    FollowerDetailsPresenter getPresenter();
}
