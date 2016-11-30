package co.netguru.android.inbbbox.feature.buckets.details;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;

public interface BucketDetailsContract {
    interface View extends MvpView {

        void setFragmentTitle(String string);

        void showShots(List<ShotEntity> shotEntities);

        void showLoadingMoreShotsView();

        void hideLoadingMoreShotsView();

        void addShots(List<ShotEntity> shotEntities);

        void showEmptyView();

        void hideProgressbar();
    }

    interface Presenter extends MvpPresenter<View> {

        void handleBucketData(BucketWithShots bucketWithShots, int perPage);

        void loadMoreShots();

        void refreshShots();
    }
}
