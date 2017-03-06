package co.netguru.android.inbbbox.feature.shared.peekandpop;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotPeekAndPopContract {

    interface View extends MvpView {
        void showMessageOnServerError(String errorMessage);
        void showMessageShotLiked();
        void showMessageShotUnliked();
    }

    interface Presenter extends MvpPresenter<View> {
        void toggleLikeShot(Shot shot);
        void detach();
    }
}
