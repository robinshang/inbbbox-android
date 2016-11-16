package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.Shot;

interface ShotsContract {

    interface View extends MvpView {

        void showItems(List<Shot> items);

        void showError(String error);

        void hideLoadingIndicator();

        void changeShotLikeStatus(Shot shot);

        void closeFabMenu();

        void showShotDetails(int id);
    }

    interface Presenter extends MvpPresenter<View> {
        void likeShot(int shotPosition);

        void loadData();

        void showShotDetails(int itemPosition);
    }
}
