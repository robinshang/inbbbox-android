package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.model.ui.Follower;

interface FollowerDetailsContract {

    interface View extends MvpView {
        void showFollowerData(Follower follower);
    }

    interface Presenter extends MvpPresenter<View> {
        void followerDataReceived(Follower follower);
    }
}
