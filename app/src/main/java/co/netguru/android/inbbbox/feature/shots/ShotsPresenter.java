package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ShotsController;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotsPresenter extends MvpNullObjectBasePresenter<ShotsContract.View>
        implements ShotsContract.Presenter {

    private final ShotsController shotsController;
    private final ErrorMessageController errorMessageController;
    private final LikeShotController likeShotController;
    private final CompositeSubscription subscriptions;
    private List<Shot> shotItems;

    @Inject
    ShotsPresenter(ShotsController shotsController, ErrorMessageController errorMessageController,
                   LikeShotController likeShotController) {

        this.shotsController = shotsController;
        this.errorMessageController = errorMessageController;
        this.likeShotController = likeShotController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    private void getShotsData() {
        final Subscription subscription = shotsController.getShots()
                .compose(androidIO())
                .subscribe(this::showRetrievedItems,
                        this::handleException);
        subscriptions.add(subscription);
    }

    private void showRetrievedItems(List<Shot> shotsList) {
        Timber.d("Shots received!");
        shotItems = shotsList;
        getView().showItems(shotItems);
        getView().hideLoadingIndicator();
    }

    private void handleException(Throwable exception) {
        Timber.e(exception, "Shots item receiving exception ");
        getView().hideLoadingIndicator();
        getView().showError(errorMessageController.getError(exception));
    }

    private void likeShot(Shot shot) {
        if (!shot.isLiked()) {
            final Subscription subscription = likeShotController.likeShot(shot.id())
                    .compose(androidIO())
                    .subscribe(aVoid -> {
                    }, this::onShotLikeError, () -> onShotLikeCompleted(shot));
            subscriptions.add(subscription);
        }
    }

    private void onShotLikeCompleted(Shot shot) {
        Timber.d("Shot liked : %s", shot);
        getView().changeShotLikeStatus(changeShotLikeStatus(shot));
    }

    private void onShotLikeError(Throwable throwable) {
        Timber.e(throwable, "Error while sending shot like");
        getView().showError(errorMessageController.getError(throwable));
    }

    private Shot changeShotLikeStatus(Shot shot) {
        return Shot.builder()
                .id(shot.id())
                .title(shot.title())
                .description(shot.description())
                .hdpiImageUrl(shot.hdpiImageUrl())
                .normalImageUrl(shot.normalImageUrl())
                .thumbnailUrl(shot.thumbnailUrl())
                .isLiked(true)
                .build();
    }

    @Override
    public void likeShot(int shotPosition) {
        likeShot(shotItems.get(shotPosition));
        getView().closeFabMenu();
    }

    @Override
    public void loadData() {
        getShotsData();
    }
}
