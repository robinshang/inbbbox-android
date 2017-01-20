package co.netguru.android.inbbbox.feature.team;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;

public class TeamDetailsContract {

    interface View extends MvpView, MvpLceView<List<UserWithShots>> {
        void hideProgressBars();

        void showMoreUsers(List<UserWithShots> users);

        void showMessageOnServerError(String message);

        void setFollowingMenuIcon(boolean isFollowing);

        void showUnfollowDialog(String name);
    }

    interface Presenter extends MvpPresenter<TeamDetailsContract.View> {
        void checkIfTeamIsFollowed(UserWithShots team);

        void getMoreMembers(UserWithShots team);

        void loadTeamData(UserWithShots team);

        void onUnfollowClick();

        void onFollowClick();

        void unfollowUser();
    }
}
