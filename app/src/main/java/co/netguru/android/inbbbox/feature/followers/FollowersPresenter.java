package co.netguru.android.inbbbox.feature.followers;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;

@FragmentScope
public class FollowersPresenter extends MvpNullObjectBasePresenter<FollowersContract.View>
        implements FollowersContract.Presenter {

    private final FollowersProvider followersProvider;

    @Inject
    public FollowersPresenter(FollowersProvider followersProvider) {
        this.followersProvider = followersProvider;
    }
}
