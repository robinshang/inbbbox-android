package co.netguru.android.inbbbox.feature.bucket.detail;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

public interface BucketDetailsContract {
    interface View extends MvpView, HttpErrorView, MvpLceView<List<ShotEntity>> {

        void setFragmentTitle(String string);

        void showLoadingMoreShotsView();

        void hideLoadingMoreShotsView();

        void addShots(List<ShotEntity> shotEntities);

        void showEmptyView();

        void hideEmptyView();

        void hideProgressbar();

        void showRemoveBucketDialog(@NonNull String bucketName);

        void showRefreshedBucketsView(long deletedBucketId);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void handleBucketData(BucketWithShots bucketWithShots, int perPage);

        void loadMoreShots();

        void refreshShots();

        void checkDataEmpty(List<ShotEntity> data);

        void onDeleteBucketClick();

        void deleteBucket();

    }
}
