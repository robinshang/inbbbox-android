package co.netguru.android.inbbbox.feature.buckets;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.BucketWithShots;

public interface BucketsFragmentContract {

    interface View extends MvpView {

        void showBucketsWithShots(List<BucketWithShots> bucketsWithShots);

        void hideProgressBars();

        void addMoreBucketsWithShots(List<BucketWithShots> bucketWithShotsList);

        void showEmptyBucketView();

        void showLoadingMoreBucketsView();

        void hideLoadingMoreBucketsView();

        void showDetailedBucketView(BucketWithShots bucketWithShots, int bucketShotsPerPageCount);

        void openCreateDialogFragment();

        void addNewBucketWithShotsOnTop(BucketWithShots bucketWithShots);

        void scrollToTop();
    }

    interface Presenter extends MvpPresenter<BucketsFragmentContract.View> {

        void loadBucketsWithShots();

        void loadMoreBucketsWithShots();

        void handleBucketWithShotsClick(BucketWithShots bucketWithShots);

        void handleCreateBucket();
    }
}
