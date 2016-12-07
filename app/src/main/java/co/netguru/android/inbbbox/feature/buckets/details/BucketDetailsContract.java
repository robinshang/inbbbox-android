package co.netguru.android.inbbbox.feature.buckets.details;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;

public interface BucketDetailsContract {
    interface View extends BaseMvpRestView, MvpLceView<List<ShotEntity>> {

        void setFragmentTitle(String string);

        void showLoadingMoreShotsView();

        void hideLoadingMoreShotsView();

        void addShots(List<ShotEntity> shotEntities);

        void showEmptyView();

        void hideEmptyView();

        void hideProgressbar();

        void showRemoveBucketDialog(@NonNull String bucketName);

        void showRefreshedBucketsView();
    }

    interface Presenter extends BaseMvpRestPresenter<View> {

        void handleBucketData(BucketWithShots bucketWithShots, int perPage);

        void loadMoreShots();

        void refreshShots();

        void checkDataEmpty(List<ShotEntity> data);

        void onDeleteBucketClick();

        void deleteBucket();

    }
}
