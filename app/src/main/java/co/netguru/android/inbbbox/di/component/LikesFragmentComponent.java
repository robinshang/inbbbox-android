package co.netguru.android.inbbbox.di.component;

import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.di.scope.FragmentScope;
import co.netguru.android.inbbbox.feature.likes.LikesFragment;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = LikesFragmentModule.class)
public interface LikesFragmentComponent {

    void inject(LikesFragment fragment);
}
