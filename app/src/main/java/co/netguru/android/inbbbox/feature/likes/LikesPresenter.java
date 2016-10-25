package co.netguru.android.inbbbox.feature.likes;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.di.scope.FragmentScope;

@FragmentScope
public final class LikesPresenter extends MvpNullObjectBasePresenter<LikesViewContract.View>
        implements LikesViewContract.Presenter {

    private final LikesProvider likesProvider;

    @Inject
    LikesPresenter(LikesProvider likesProvider) {
        this.likesProvider = likesProvider;
    }
}
