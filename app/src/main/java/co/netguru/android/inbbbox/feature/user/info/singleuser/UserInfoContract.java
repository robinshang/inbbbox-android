package co.netguru.android.inbbbox.feature.user.info.singleuser;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface UserInfoContract {

    interface View extends MvpView {
        void showTeams(List<User> teams);
        void openTeam(User user);
        void openShot(Shot shot);
        void showShots(List<Shot> shots);
        void showMessageOnServerError(String message);
    }

    interface Presenter extends MvpPresenter<View> {
        void onTeamClick(User team);
        void onShotClick(Shot shot);
    }
}
