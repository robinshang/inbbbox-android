package co.netguru.android.inbbbox.feature.user.info.team;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

interface TeamInfoContract {

    interface View extends MvpView {
        void showTeamMembers(List<UserWithShots> users);

        void showMoreTeamMembers(List<UserWithShots> users);

        void openShotDetails(Shot shot);

        void openUserDetails(User user);

        void showLoadingMoreTeamMembersView();

        void hideLoadingMoreTeamMembersView();

        void showMessageOnServerError(String message);

        void openUrl(String url);
    }

    interface Presenter extends MvpPresenter<View> {
        void onShotClick(Shot shot);

        void onUserClick(User user);

        void loadMoreTeamMembers();

        void onLinkClick(String url);
    }
}