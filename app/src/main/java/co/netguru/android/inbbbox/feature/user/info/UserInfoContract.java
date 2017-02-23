package co.netguru.android.inbbbox.feature.user.info;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;

public interface UserInfoContract {

    interface View extends MvpView {
        void showData(List<UserWithShots> users);
    }

    interface Presenter extends MvpPresenter<View> {

    }
}
