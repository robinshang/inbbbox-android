package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.feature.likes.LikesFragment;
import co.netguru.android.inbbbox.feature.likes.LikesPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = LikesFragmentModule.class)
public interface LikesFragmentComponent {

    void inject(LikesFragment fragment);

    LikesPresenter getPresenter();
}
