package co.netguru.android.inbbbox.feature.bucket.currentuser;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

public interface BucketsFragmentContract {

    interface View extends HttpErrorView, MvpLceView<List<BucketWithShots>> {

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

        void loadBucketsWithShots(boolean tryFromCache);

        void loadMoreBucketsWithShots();

        void handleBucketWithShotsClick(BucketWithShots bucketWithShots);

        void handleCreateBucket();

        void checkEmptyData(List<BucketWithShots> data);

        void handleDeleteBucket(long deletedBucketId);

        void refreshBuckets();
    }
}
