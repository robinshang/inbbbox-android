package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.feature.shots.like.LikeResponseMapper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotsPresenter extends MvpNullObjectBasePresenter<ShotsContract.View>
        implements ShotsContract.Presenter {

    private final ShotsProvider shotsProvider;
    private final ErrorMessageParser errorMessageParser;
    private final LikeResponseMapper likeResponseMapper;
    private final CompositeSubscription subscriptions;

    @Inject
    ShotsPresenter(ShotsProvider shotsProvider, ErrorMessageParser errorMessageParser,
                   LikeResponseMapper likeResponseMapper) {

        this.shotsProvider = shotsProvider;
        this.errorMessageParser = errorMessageParser;
        this.likeResponseMapper = likeResponseMapper;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void likeShot(Shot shot) {
        if (!shot.isLiked()) {
            final Subscription subscription = likeResponseMapper.likeShot(shot.id())
                    .compose(androidIO())
                    .subscribe(aVoid -> {}, this::onShotLikeError, () -> onShotLikeCompleted(shot));
            subscriptions.add(subscription);
        }
    }

    private void getShotsData() {
        final Subscription subscription = shotsProvider.getShots()
                .compose(androidIO())
                .subscribe(this::showRetrievedItems,
                        this::handleException);
        subscriptions.add(subscription);
    }

    private void showRetrievedItems(List<Shot> shotsList) {
        Timber.d("Shots received!");
        getView().showItems(shotsList);
        getView().hideLoadingIndicator();
    }

    private void handleException(Throwable exception) {
        Timber.e(exception, "Shots item receiving exception ");
        getView().hideLoadingIndicator();
        getView().showError(errorMessageParser.getError(exception));
    }

    private void onShotLikeCompleted(Shot shot) {
        Timber.d("Shot liked : %s", shot);
        getView().changeShotLikeStatus(changeShotLikeStatus(shot));
    }

    private void onShotLikeError(Throwable throwable) {
        Timber.e(throwable, "Error while sending shot like");
        getView().showError(errorMessageParser.getError(throwable));
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
    public void loadData() {
        getShotsData();
    }
}
