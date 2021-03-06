package co.netguru.android.inbbbox.feature.user.buckets;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

interface UserBucketsContract {

    interface View extends MvpLceView<List<BucketWithShots>> {
        void showMessageOnServerError(String error);

        void showLoadingMoreBucketsView();

        void hideProgressBars();

        void hideLoadingMoreBucketsView();

        void addMoreBucketsWithShots(List<BucketWithShots> bucketsWithShotsList);

        void showDetailedBucketView(BucketWithShots bucketWithShots, int bucketShotsPerPageCount);

        void showEmptyView();

        void hideEmptyView();

        void showShotDetails(Shot shot, List<Shot> allShots);

        void addMoreBucketShots(long bucketId, List<Shot> newShots, int shotsPerPage);
    }

    interface Presenter extends MvpPresenter<View> {

        void loadBucketsWithShots(boolean tryFromCache);

        void loadMoreBucketsWithShots();

        void handleBucketWithShotsClick(BucketWithShots bucketWithShots);

        void refreshBuckets();

        void showContentForData(List<BucketWithShots> bucketsWithShotsList);

        void onShotClick(Shot shot, BucketWithShots bucketWithShots);

        void getMoreShotsFromBucket(BucketWithShots bucketWithShots);

    }
}
