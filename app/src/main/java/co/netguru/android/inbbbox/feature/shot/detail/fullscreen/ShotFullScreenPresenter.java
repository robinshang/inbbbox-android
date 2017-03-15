package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class ShotFullScreenPresenter extends MvpNullObjectBasePresenter<ShotFullscreenContract.View>
        implements ShotFullscreenContract.Presenter {

    private static final int SHOTS_PER_PAGE = 15;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final ShotsController shotsController;
    private final LikeShotController likedShotsController;
    private final BucketsController bucketsController;
    private final UserShotsController userShotsController;
    private final ShotDetailsRequest detailsRequest;
    private final List<Shot> allShots;
    private final int previewShotIndex;
    private int currentPage;
    private boolean hasMore = true;

    @Inject
    ShotFullScreenPresenter(ShotsController shotsController, LikeShotController likedShotsController,
                            BucketsController bucketsController, UserShotsController userShotsController,
                            List<Shot> allShots, int previewShotIndex, ShotDetailsRequest shotDetailsRequest) {
        this.shotsController = shotsController;
        this.likedShotsController = likedShotsController;
        this.bucketsController = bucketsController;
        this.userShotsController = userShotsController;
        this.detailsRequest = shotDetailsRequest;
        this.previewShotIndex = previewShotIndex;
        this.allShots = allShots;
    }

    @Override
    public void attachView(ShotFullscreenContract.View view) {
        super.attachView(view);

        currentPage = allShots.size() / SHOTS_PER_PAGE;
        hasMore = allShots.size() % SHOTS_PER_PAGE == 0;

        if (!allShots.isEmpty()) {
            getView().previewShots(allShots, previewShotIndex);
        }
    }

    @Override
    public void onRequestMoreData() {
        if (hasMore) {
            currentPage++;
            Observable<List<Shot>> requestMoreObservable;

            switch (detailsRequest.detailsType()) {
                case ShotDetailsType.DEFAULT:
                    requestMoreObservable = shotsController.getShots(currentPage, SHOTS_PER_PAGE);
                    break;
                case ShotDetailsType.LIKES:
                    requestMoreObservable = likedShotsController
                            .getLikedShots(currentPage, SHOTS_PER_PAGE);
                    break;
                case ShotDetailsType.BUCKET:
                    requestMoreObservable = bucketsController.getShotsListFromBucket(
                            detailsRequest.id(), currentPage, SHOTS_PER_PAGE, false)
                                                    .toObservable();
                    break;
                case ShotDetailsType.USER:
                    requestMoreObservable = userShotsController
                            .getUserShotsList(detailsRequest.id(), currentPage, SHOTS_PER_PAGE);
                    break;
                default:
                    return;
            }

            subscriptions.add(
                    requestMoreObservable
                            .compose(androidIO())
                            .subscribe(shotList -> {
                                hasMore = shotList.size() >= SHOTS_PER_PAGE;
                                getView().showMoreItems(shotList);
                            }, throwable -> Timber.e(throwable,
                                    "Error occurred when getting more items")));
        }
    }

    @Override
    public void onBackArrowPressed() {
        getView().close();
    }

    @Override
    public void detachView(boolean retainInstance) {
        subscriptions.clear();
        super.detachView(retainInstance);
    }
}
