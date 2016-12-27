package co.netguru.android.inbbbox.feature.bucket;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.common.base.ErrorPresenter;
import co.netguru.android.inbbbox.common.base.HttpErrorView;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;

public interface BucketsFragmentContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<BucketWithShots>> {

        void hideProgressBars();

        void addMoreBucketsWithShots(List<BucketWithShots> bucketWithShotsList);

        void hideEmptyBucketView();

        void showEmptyBucketView();

        void showLoadingMoreBucketsView();

        void hideLoadingMoreBucketsView();

        void showDetailedBucketView(BucketWithShots bucketWithShots, int bucketShotsPerPageCount);

        void openCreateDialogFragment();

        void addNewBucketWithShotsOnTop(BucketWithShots bucketWithShots);

        void scrollToTop();

        void removeBucketFromList(long bucketId);
    }

    interface Presenter extends MvpPresenter<BucketsFragmentContract.View>, ErrorPresenter {

        void loadBucketsWithShots();

        void loadMoreBucketsWithShots();

        void handleBucketWithShotsClick(BucketWithShots bucketWithShots);

        void handleCreateBucket();

        void checkEmptyData(List<BucketWithShots> data);

        void handleDeleteBucket(long deletedBucketId);
    }
}
