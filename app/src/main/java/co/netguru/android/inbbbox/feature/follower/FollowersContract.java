package co.netguru.android.inbbbox.feature.follower;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface FollowersContract {

    interface View extends HttpErrorView, MvpLceView<List<UserWithShots>> {

        void showMoreFollowedUsers(List<UserWithShots> userWithShotsList);

        void hideLoadingMoreFollowersView();

        void hideEmptyFollowersInfo();

        void showEmptyFollowersInfo();

        void showLoadingMoreFollowersView();

        void hideProgressBars();

        void openUserDetails(UserWithShots userWithShots);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void getFollowedUsersFromServer(boolean canUseCacheForShots);

        void getMoreFollowedUsersFromServer();

        void checkDataEmpty(List<UserWithShots> data);

        void onFollowedUserSelect(UserWithShots userWithShots);

        void refreshFollowedUsers();
    }
}
