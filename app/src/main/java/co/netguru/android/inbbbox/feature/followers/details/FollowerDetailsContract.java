package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.ErrorPresenter;
import co.netguru.android.inbbbox.feature.common.HttpErrorView;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.User;

public interface FollowerDetailsContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<Shot>> {
        void showFollowerData(Follower follower);

        void showMoreUserShots(List<Shot> shotList);

        void showFollowersList();

        void hideProgress();

        void openShotDetailsScreen(Shot shot);

        void showUnFollowDialog(String username);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {
        void followerDataReceived(Follower follower);

        void userDataReceived(User user);

        void refreshUserShots();

        void getMoreUserShotsFromServer();

        void onUnFollowClick();

        void unFollowUser();

        void showShotDetails(Shot shot);
    }
}
