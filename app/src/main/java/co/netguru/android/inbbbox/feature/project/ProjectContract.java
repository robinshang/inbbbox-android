package co.netguru.android.inbbbox.feature.project;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

interface ProjectContract {

    interface View extends MvpView, MvpLceView<List<Shot>> {
        void addShots(List<Shot> shots);

        void showMessageOnServerError(String message);

        void showLoadingMoreShotsView();

        void showShotDetails(Shot shot);

        void hideProgressBar();
    }

    interface Presenter extends MvpPresenter<View> {

        void refreshShots();

        void loadMoreShots();

        void onShotClick(Shot shot);
    }
}
