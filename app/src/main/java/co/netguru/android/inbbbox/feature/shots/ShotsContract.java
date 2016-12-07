package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;

interface ShotsContract {

    interface View extends BaseMvpRestView, MvpLceView<List<Shot>> {

        void showMoreItems(List<Shot> items);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void changeShotLikeStatus(Shot shot);

        void closeFabMenu();

        void showShotDetails(Shot shot);

        void showBucketChoosing(Shot shot);

        void showBucketAddSuccess();

        void showDetailsScreenInCommentMode(Shot selectedShot);
    }

    interface Presenter extends BaseMvpRestPresenter<View> {

        void likeShot(Shot shot);

        void getShotsFromServer();

        void getMoreShotsFromServer();

        void handleAddShotToBucket(Shot shot);

        void addShotToBucket(Bucket bucket, Shot shot);

        void showShotDetails(Shot shot);

        void showCommentInput(Shot selectedShot);
    }
}
