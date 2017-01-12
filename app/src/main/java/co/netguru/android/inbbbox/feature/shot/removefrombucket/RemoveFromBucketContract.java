package co.netguru.android.inbbbox.feature.shot.removefrombucket;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

public interface RemoveFromBucketContract {
    interface View extends MvpView, HttpErrorView {

        void setShotTitle(String title);

        void showAuthorAvatar(String url);

        void showShotPreview(String url);

        void showShotAuthorAndTeam(String authorName, String teamName);

        void showShotAuthor(String authorName);

        void showShotCreationDate(ZonedDateTime dateTime);

        void showBucketListLoading();

        void setBucketsList(List<Bucket> buckets);

        void hideProgressBar();

        void passResultAndCloseFragment(Bucket bucket, Shot shot);

        void openShotFullscreen(Shot shot);

        void showBucketListLoadingMore();

        void showMoreBuckets(List<Bucket> buckets);

        void showBucketsList();
    }

    interface Presenter extends MvpPresenter<RemoveFromBucketContract.View>, ErrorPresenter {

        void handleShot(Shot shot);

        void loadAvailableBuckets();

        void loadMoreBuckets();

        void handleBucketClick(Bucket bucket);

        void onOpenShotFullscreen();
    }
}
