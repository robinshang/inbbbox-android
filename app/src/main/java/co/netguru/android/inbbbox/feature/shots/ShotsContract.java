package co.netguru.android.inbbbox.feature.shots;

import android.support.annotation.StringRes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.data.ui.Shot;

interface ShotsContract {

    interface View extends MvpView {

        void showItems(List<Shot> items);

        void showError(String error);

        void showMessage(@StringRes int res);

        void changeShotLikeStatus(Shot shot);
    }

    interface Presenter extends MvpPresenter<View> {
        void likeShot(Shot shot);
    }
}
