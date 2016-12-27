package co.netguru.android.inbbbox.feature.like;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface LikesFragmentComponent {

    void inject(LikesFragment fragment);

    LikesPresenter getPresenter();
}
