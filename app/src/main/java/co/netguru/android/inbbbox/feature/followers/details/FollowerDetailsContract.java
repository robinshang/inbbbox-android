package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.User;

interface FollowerDetailsContract {

    interface View extends MvpView {
        void showFollowerData(Follower follower);

        void showMoreUserShots(List<Shot> shotList);

        void showFollowersList();

        void showError(String message);

        void openShotDetailsScreen(Shot shot);

        void showUnFollowDialog(String username);

        void showUserData(User user, List<Shot> list);
    }

    interface Presenter extends MvpPresenter<View> {
        void followerDataReceived(Follower follower);

        void getMoreUserShotsFromServer();

        void onUnFollowClick();

        void unFollowUser();

        void showShotDetails(Shot shot);

        void userDataReceived(User user);
    }
}
