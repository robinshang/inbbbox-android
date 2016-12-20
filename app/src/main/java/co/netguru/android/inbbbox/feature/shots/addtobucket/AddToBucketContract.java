package co.netguru.android.inbbbox.feature.shots.addtobucket;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.ErrorPresenter;
import co.netguru.android.inbbbox.feature.common.HttpErrorView;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;

public interface AddToBucketContract {
    interface View extends MvpView, HttpErrorView {

        void setShotTitle(String title);

        void showAuthorAvatar(String url);

        void showShotPreview(String url);

        void showShotAuthorAndTeam(String authorName, String teamName);

        void showShotAuthor(String authorName);

        void showShotCreationDate(LocalDateTime localDateTime);

        void showBucketListLoading();

        void showNoBucketsAvailable();

        void setBucketsList(List<Bucket> buckets);

        void hideProgressBar();

        void passResultAndCloseFragment(Bucket bucket, Shot shot);

        void openShotFullscreen(Shot shot);

        void showBucketListLoadingMore();

        void showMoreBuckets(List<Bucket> buckets);

        void addNewBucketOnTop(Bucket bucket);

        void scrollToTop();

        void showCreateBucketView();

        void showBucketsList();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void handleShot(Shot shot);

        void loadAvailableBuckets();

        void loadMoreBuckets();

        void handleBucketClick(Bucket bucket);

        void onOpenShotFullscreen();

        void createBucket();
    }
}
