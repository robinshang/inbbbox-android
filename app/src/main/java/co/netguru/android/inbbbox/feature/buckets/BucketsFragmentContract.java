package co.netguru.android.inbbbox.feature.buckets;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;

public interface BucketsFragmentContract {

    interface View extends BaseMvpRestView, MvpLceView<List<BucketWithShots>> {

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

    interface Presenter extends BaseMvpRestPresenter<BucketsFragmentContract.View> {

        void loadBucketsWithShots();

        void loadMoreBucketsWithShots();

        void handleBucketWithShotsClick(BucketWithShots bucketWithShots);

        void handleCreateBucket();

        void checkEmptyData(List<BucketWithShots> data);
    }
}
