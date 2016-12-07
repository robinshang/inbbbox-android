package co.netguru.android.inbbbox.feature.shots.addtobucket;


import org.threeten.bp.LocalDateTime;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;

public interface AddToBucketContract {
    interface View extends BaseMvpRestView {

        void setShotTitle(String title);

        void showAuthorAvatar(String url);

        void showShotPreview(String url);

        void showShotAuthorAndTeam(String authorName, String teamName);

        void showShotAuthor(String authorName);

        void showShotCreationDate(LocalDateTime localDateTime);

        void showBucketListLoading();

        void showNoBucketsAvailable();

        void showBuckets(List<Bucket> buckets);

        void passResultAndCloseFragment(Bucket bucket, Shot shot);
    }

    interface Presenter extends BaseMvpRestPresenter<View> {

        void handleShot(Shot shot);

        void loadAvailableBuckets();

        void handleBucketClick(Bucket bucket);
    }
}
