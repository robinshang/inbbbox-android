package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.bucket.BucketsController;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
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
    private final Shot shot;
    private final List<Shot> allShots;
    private int currentPage;
    private boolean hasMore = true;

    @Inject
    public ShotFullScreenPresenter(ShotsController shotsController, LikeShotController likedShotsController,
                                   BucketsController bucketsController, UserShotsController userShotsController,
                                   Shot shot, List<Shot> allShots, ShotDetailsRequest shotDetailsRequest) {
        this.shotsController = shotsController;
        this.likedShotsController = likedShotsController;
        this.bucketsController = bucketsController;
        this.userShotsController = userShotsController;
        this.detailsRequest = shotDetailsRequest;
        this.shot = shot;
        this.allShots = allShots;
    }

    @Override
    public void attachView(ShotFullscreenContract.View view) {
        super.attachView(view);

        if (allShots != null && !allShots.isEmpty()) {
            currentPage = allShots.size() / SHOTS_PER_PAGE;
            hasMore = allShots.size() % SHOTS_PER_PAGE == 0;

            getView().previewShots(shot, allShots);
        } else {
            getView().previewSingleShot(shot);
        }
    }

    @Override
    public void onRequestMoreData() {
        if (hasMore) {
            currentPage++;
            Observable<List<Shot>> requestMoreObservable;

            switch (detailsRequest.detailsType()) {
                case DEFAULT:
                    requestMoreObservable = shotsController.getShots(currentPage, SHOTS_PER_PAGE);
                    break;
                case LIKES:
                    requestMoreObservable = likedShotsController.getLikedShots(currentPage, SHOTS_PER_PAGE);
                    break;
                case BUCKET:
                    requestMoreObservable = bucketsController.getShotsListFromBucket(detailsRequest.id(), currentPage, SHOTS_PER_PAGE)
                            .toObservable()
                            .flatMap(Observable::from)
                            .map(Shot::create)
                            .toList();
                    break;
                case USER:
                    requestMoreObservable = userShotsController.getUserShotsList(detailsRequest.id(), currentPage, SHOTS_PER_PAGE);
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
