package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.ui.LikedShot;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.event.LikeRefreshEvent;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.feature.likes.LikedShotsProvider;
import co.netguru.android.inbbbox.feature.shots.like.LikeResponseMapper;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotsPresenter extends MvpNullObjectBasePresenter<ShotsContract.View>
        implements ShotsContract.Presenter {

    private final ShotsProvider shotsProvider;
    private final LikedShotsProvider likedShotsProvider;
    private final ErrorMessageParser errorMessageParser;
    private final LikeResponseMapper likeResponseMapper;
    private final CompositeSubscription subscriptions;
    private List<Shot> items;

    @Inject
    ShotsPresenter(ShotsProvider shotsProvider, LikedShotsProvider likedShotsProvider,
                   ErrorMessageParser errorMessageParser, LikeResponseMapper likeResponseMapper) {

        this.shotsProvider = shotsProvider;
        this.likedShotsProvider = likedShotsProvider;
        this.errorMessageParser = errorMessageParser;
        this.likeResponseMapper = likeResponseMapper;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(ShotsContract.View view) {
        super.attachView(view);
        getShotsData();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void likeShot(Shot shot) {
        final Subscription subscription = likeResponseMapper.likeShot(shot.id())
                .compose(androidIO())
                .subscribe(status -> onShotLikeNext(shot, status),
                        throwable -> Timber.e(throwable, "Error while sending shot like"));
        subscriptions.add(subscription);
    }

    private void getShotsData() {
        final Subscription subscription = Observable.zip(shotsProvider.getShots(),
                likedShotsProvider.getLikedShots().map(LikedShot::getId).toList(), this::setShotLikeStatus)
                .compose(androidIO())
                .subscribe(this::showRetrievedItems,
                        this::handleException);
        subscriptions.add(subscription);
    }

    private List<Shot> setShotLikeStatus(List<Shot> shots, List<Integer> likedShots) {
        final List<Shot> finalList = new LinkedList<>();
        for (final Shot shot : shots) {
            finalList.add(createShot(shot, likedShots.contains(shot.id())));
        }
        return finalList;
    }

    private Shot createShot(Shot shot, boolean isLiked) {
        return Shot.builder()
                .id(shot.id())
                .title(shot.title())
                .description(shot.description())
                .hdpiImageUrl(shot.hdpiImageUrl())
                .normalImageUrl(shot.normalImageUrl())
                .thumbnailUrl(shot.thumbnailUrl())
                .likeStatus(isLiked ? Shot.LIKED : Shot.UNLIKED)
                .build();
    }

    private void showRetrievedItems(List<Shot> shotsList) {
        Timber.d("Shots received!");
        this.items = shotsList;
        getView().showItems(shotsList);
    }

    private void handleException(Throwable exception) {
        Timber.e(exception, "Shots item receiving exception ");
        getView().showError(errorMessageParser.getError(exception));
    }

    private void onShotLikeNext(Shot shot, boolean status) {
        Timber.d("Shot liked : %s", status);
        if (status) {
            getView().changeShotLikeStatus(createShot(shot, true));
            EventBus.getDefault().post(new LikeRefreshEvent());
        }
    }
}
