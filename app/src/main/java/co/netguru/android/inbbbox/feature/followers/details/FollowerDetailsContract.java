package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

interface FollowerDetailsContract {

    interface View extends MvpLceView<List<Shot>> {
        void showFollowerData(Follower follower);

        void showMoreUserShots(List<Shot> shotList);

        void showFollowersList();

        void showError(String message);

        void hideProgress();

        void openShotDetailsScreen(Shot shot);

        void showUnFollowDialog(String username);
    }

    interface Presenter extends MvpPresenter<View> {
        void followerDataReceived(Follower follower);

        void refreshUserShots();

        void getMoreUserShotsFromServer();

        void onUnFollowClick();

        void unFollowUser();

        void showShotDetails(Shot shot);
    }
}
