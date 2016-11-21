package co.netguru.android.inbbbox.feature.shots.addtobucket;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;

public interface AddToBucketContract {
    interface View extends MvpView {

        void setShotTitle(String title);

        void showAuthorAvatar(String url);

        void showShotPreview(String url);

        void showShotAuthorAndTeam(String authorName, String teamName);

        void showShotAuthor(String authorName);

        void showShotCreationDate(LocalDateTime localDateTime);

        void showApiError();

        void showBucketListLoading();

        void showNoBucketsAvailable();

        void showBuckets(List<Bucket> buckets);

        void passResultAndCloseFragment(Bucket bucket, Shot shot);
    }

    interface Presenter extends MvpPresenter<View> {

        void handleShot(Shot shot);

        void loadAvailableBuckets();

        void handleBucketClick(Bucket bucket);
    }
}
