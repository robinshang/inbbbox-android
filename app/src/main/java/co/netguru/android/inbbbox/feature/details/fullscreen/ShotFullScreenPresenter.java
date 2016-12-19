package co.netguru.android.inbbbox.feature.details.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.feature.details.ShotDetailsRequest;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotFullScreenPresenter extends MvpNullObjectBasePresenter<ShotFullscreenContract.View>
        implements ShotFullscreenContract.Presenter {

    private static final int SHOTS_PER_PAGE = 15;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final ShotsController shotsController;
    private final LikedShotsController likedShotsController;
    private final BucketsController bucketsController;
    private final UserShotsController userShotsController;

    private int currentPage;
    private boolean hasMore = true;
    private ShotDetailsRequest detailsRequest;

    @Inject
    public ShotFullScreenPresenter(ShotsController shotsController, LikedShotsController likedShotsController,
                                   BucketsController bucketsController, UserShotsController userShotsController) {
        this.shotsController = shotsController;
        this.likedShotsController = likedShotsController;
        this.bucketsController = bucketsController;
        this.userShotsController = userShotsController;
    }

    @Override
    public void onViewCreated(Shot shot, List<Shot> allShots, ShotDetailsRequest detailsRequest) {
        this.detailsRequest = detailsRequest;

        if (allShots != null && allShots.size() > 0) {
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
                    requestMoreObservable = likedShotsController.getLikedShots(currentPage, SHOTS_PER_PAGE)
                            .toList();
                    break;
                case BUCKET:
                    requestMoreObservable = bucketsController.getShotsListFromBucket(detailsRequest.id(), currentPage, SHOTS_PER_PAGE)
                            .toObservable()
                            .flatMap(Observable::from)
                            .map(shotEntity -> Shot.create(shotEntity))
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
