package co.netguru.android.inbbbox.like;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.like.LikesFragment;
import co.netguru.android.inbbbox.like.LikesPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface LikesFragmentComponent {

    void inject(LikesFragment fragment);

    LikesPresenter getPresenter();
}
