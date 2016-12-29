package co.netguru.android.inbbbox.feature.buckets.details;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.ErrorPresenter;
import co.netguru.android.inbbbox.feature.common.HttpErrorView;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;

public interface BucketDetailsContract {
    interface View extends MvpView, HttpErrorView, MvpLceView<List<Shot>> {

        void setFragmentTitle(String string);

        void showLoadingMoreShotsView();

        void hideLoadingMoreShotsView();

        void addShots(List<Shot> shots);

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

        void checkDataEmpty(List<Shot> data);

        void onDeleteBucketClick();

        void deleteBucket();

    }
}
