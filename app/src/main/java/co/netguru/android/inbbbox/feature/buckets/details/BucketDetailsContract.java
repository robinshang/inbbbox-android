package co.netguru.android.inbbbox.feature.buckets.details;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;

public interface BucketDetailsContract {
    interface View extends MvpLceView<List<ShotEntity>> {

        void setFragmentTitle(String string);

        void showLoadingMoreShotsView();

        void hideLoadingMoreShotsView();

        void addShots(List<ShotEntity> shotEntities);

        void showEmptyView();

        void hideEmptyView();

        void hideProgressbar();

        void showRemoveBucketDialog(@NonNull String bucketName);

        void showError(String message);

        void showRefreshedBucketsView();
    }

    interface Presenter extends MvpPresenter<View> {

        void handleBucketData(BucketWithShots bucketWithShots, int perPage);

        void loadMoreShots();

        void refreshShots();

        void checkDataEmpty(List<ShotEntity> data);

        void onDeleteBucketClick();

        void deleteBucket();

    }
}
