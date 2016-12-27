package co.netguru.android.inbbbox.feature.like;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.like.LikesFragment;
import co.netguru.android.inbbbox.feature.like.LikesPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface LikesFragmentComponent {

    void inject(LikesFragment fragment);

    LikesPresenter getPresenter();
}
