package co.netguru.android.inbbbox.feature.details.fullscreen;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotFullScreenPresenter extends MvpNullObjectBasePresenter<ShotFullscreenContract.View>
        implements ShotFullscreenContract.Presenter {

    private static final int SHOTS_PER_PAGE = 15;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final ShotsController shotsController;
    private int currentPage;
    private boolean hasMore = true;

    @Inject
    public ShotFullScreenPresenter(ShotsController shotsController) {
        this.shotsController = shotsController;
    }

    @Override
    public void onViewCreated(Shot shot, List<Shot> allShots) {
        if (allShots != null && allShots.size() > 0) {
            currentPage = allShots.size() / SHOTS_PER_PAGE;
            getView().previewShots(shot, allShots);
        } else {
            getView().previewSingleShot(shot);
        }
    }

    @Override
    public void onRequestMoreData() {
        if (hasMore) {
            currentPage++;
            subscriptions.add(
                    shotsController.getShots(currentPage, SHOTS_PER_PAGE)
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
