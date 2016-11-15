package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;

interface ShotsContract {

    interface View extends MvpView {

        void showItems(List<Shot> items);

        void showError(String error);

        void hideLoadingIndicator();

        void changeShotLikeStatus(Shot shot);

        void closeFabMenu();

        void showBucketChoosing(Shot shot);

        void showBucketAddSuccess();
    }

    interface Presenter extends MvpPresenter<View> {
        void likeShot(Shot shot);

        void loadData();

        void handleAddShotToBucket(Shot shot);

        void addShotToBucket(Bucket bucket, Shot shot);
    }
}
