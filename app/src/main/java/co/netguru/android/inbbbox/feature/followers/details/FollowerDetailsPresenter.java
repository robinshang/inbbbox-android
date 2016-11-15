package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.model.ui.Follower;
import timber.log.Timber;

@FragmentScope
public class FollowerDetailsPresenter extends MvpNullObjectBasePresenter<FollowerDetailsContract.View>
        implements FollowerDetailsContract.Presenter {

    private Follower follower;

    @Inject
    FollowerDetailsPresenter() {

    }

    @Override
    public void followerDataReceived(Follower follower) {
        Timber.d("Received follower : %s", follower);
        if (follower != null) {
            this.follower = follower;
            getView().showFollowerData(follower);
        }
    }
}
