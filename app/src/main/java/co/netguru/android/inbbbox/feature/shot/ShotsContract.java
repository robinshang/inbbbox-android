package co.netguru.android.inbbbox.feature.shot;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface ShotsContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<Shot>> {

        void showMoreItems(List<Shot> items);

        void showLoadingIndicator(boolean pullToRefresh);

        void hideLoadingIndicator();

        void closeFabMenu();

        void showShotDetails(Shot shot);

        void showBucketChoosing(Shot shot);

        void showBucketAddSuccess();

        void showShotRemoveFromBucketSuccess();

        void showDetailsScreenInCommentMode(Shot selectedShot);

        void onDetailsVisibilityChange(boolean isVisible);

        void updateShot(Shot shot);

        void showFirstShot();

        void shakeTabIcon(int position);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void likeShot(Shot shot);

        void getShotsFromServer(boolean pullToRefresh);

        void getMoreShotsFromServer();

        void handleAddShotToBucket(Shot shot);

        void addShotToBucket(Bucket bucket, Shot shot);

        void showShotDetails(Shot shot);

        void showCommentInput(Shot selectedShot);

        void handleFollowShotAuthor(Shot shot);

        void removeShotFromBuckets(List<Bucket> list, Shot shot);
    }
}
