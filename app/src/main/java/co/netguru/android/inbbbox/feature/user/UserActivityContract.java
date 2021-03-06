package co.netguru.android.inbbbox.feature.user;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

public interface UserActivityContract {
    interface View extends MvpView {
        void showFollowingAction(boolean following);

        void showMessageOnServerError(String errorText);

        void showShare(String toShare);

        void showUnfollowDialog(String username);
    }

    interface Presenter extends MvpPresenter<View> {
        void checkFollowingStatus(User user);

        void startFollowing(User user);

        void stopFollowing(User user);

        void shareUser(User user);

        void changeFollowingStatus(User user, boolean follow);
    }
}
