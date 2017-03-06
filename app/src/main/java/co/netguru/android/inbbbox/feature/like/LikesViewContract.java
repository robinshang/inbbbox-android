package co.netguru.android.inbbbox.feature.like;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface LikesViewContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<Shot>> {

        void showMoreLikes(List<Shot> likedShotList);

        void showLikeAtTop(Shot likedShot);

        void removeShotFromLikes(Shot unlikedShot);

        void showLoadingMoreLikesView();

        void hideLoadingMoreLikesView();

        void hideEmptyLikesInfo();

        void hideProgressBar();

        void showEmptyLikesInfo();

        void openShowDetailsScreen(Shot shot, List<Shot> shotList);

        void showBucketChooserView(Shot shot);

        void showBucketAddSuccess();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {
        void getLikesFromServer();

        void getMoreLikesFromServer();

        void showShotDetails(Shot shot, List<Shot> allShots);

        void checkDataEmpty(boolean isEmpty);

        void onBucketShot(Shot shot);

        void addShotToBucket(Shot shot, Bucket bucket);
    }
}
