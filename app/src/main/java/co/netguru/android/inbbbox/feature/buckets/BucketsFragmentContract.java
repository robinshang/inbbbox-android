package co.netguru.android.inbbbox.feature.buckets;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.HttpErrorPresenter;
import co.netguru.android.inbbbox.feature.common.HttpErrorView;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;

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
    }

    interface Presenter extends MvpPresenter<BucketsFragmentContract.View>, HttpErrorPresenter {

        void loadBucketsWithShots();

        void loadMoreBucketsWithShots();

        void handleBucketWithShotsClick(BucketWithShots bucketWithShots);

        void handleCreateBucket();

        void checkEmptyData(List<BucketWithShots> data);
    }
}
