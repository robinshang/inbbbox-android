package co.netguru.android.inbbbox.feature.user.shots;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

public interface UserShotsContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<Shot>> {

        void showMoreUserShots(List<Shot> shotList);

        void hideProgress();

        void openShotDetailsScreen(Shot shot, List<Shot> allShots, long userId);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void userDataReceived(User user);

        void refreshUserShots();

        void getMoreUserShotsFromServer();

        void showShotDetails(Shot shot);
    }
}
