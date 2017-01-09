package co.netguru.android.inbbbox.feature.follower.detail;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

public interface FollowerDetailsContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<Shot>> {
        void showFollowerData(UserWithShots follower);

        void showMoreUserShots(List<Shot> shotList);

        void hideProgress();

        void openShotDetailsScreen(Shot shot, List<Shot> allShots, long userId);

        void showUnFollowDialog(String username);

        void showFollowDialog(String username);

        void setFollowingMenuIcon(boolean isFollowed);

        void unfollowUserCompleted();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void userDataReceived(UserWithShots userWithShots);

        void checkIfUserIsFollowed(UserWithShots userWithShots);

        void refreshUserShots();

        void getMoreUserShotsFromServer();

        void onUnFollowClick();

        void unFollowUser();

        void showShotDetails(Shot shot);

        void onFollowClick();

        void followUser();
    }
}
