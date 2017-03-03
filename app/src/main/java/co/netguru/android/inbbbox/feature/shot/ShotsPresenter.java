package co.netguru.android.inbbbox.feature.shot;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.settings.model.CustomizationSettings;
import co.netguru.android.inbbbox.data.shot.ShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.DetailsVisibilityChangeEvent;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applyCompletableIoSchedulers;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

@FragmentScope
public class ShotsPresenter extends MvpNullObjectBasePresenter<ShotsContract.View>
        implements ShotsContract.Presenter {

    private static final int SHOTS_PER_PAGE = 15;
    private static final int FIRST_PAGE = 1;

    private final ShotsController shotsController;
    private final ErrorController errorController;
    private final LikeShotController likeShotController;
    private final BucketsController bucketsController;
    private final FollowersController followersController;
    private final CompositeSubscription subscriptions;
    private final RxBus rxBus;
    private final SettingsController settingsController;

    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadMoreSubscription;
    @NonNull
    private Subscription busSubscription;
    @NonNull
    private Subscription busUpdateShotSubscription;
    private int pageNumber = FIRST_PAGE;
    private boolean hasMore = true;

    @Inject
    ShotsPresenter(ShotsController shotsController, LikeShotController likeShotController,
                   BucketsController bucketsController, ErrorController errorController,
                   FollowersController followersController, RxBus rxBus, SettingsController settingsController) {

        this.shotsController = shotsController;
        this.likeShotController = likeShotController;
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        this.followersController = followersController;
        this.rxBus = rxBus;
        this.settingsController = settingsController;
        subscriptions = new CompositeSubscription();
        refreshSubscription = Subscriptions.unsubscribed();
        loadMoreSubscription = Subscriptions.unsubscribed();
        busSubscription = Subscriptions.unsubscribed();
        busUpdateShotSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void attachView(ShotsContract.View view) {
        super.attachView(view);
        setupRxBus();
        initShotsView();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadMoreSubscription.unsubscribe();
            busSubscription.unsubscribe();
            busUpdateShotSubscription.unsubscribe();
        }
    }

    private void initShotsView() {
        subscriptions.add(settingsController.getCustomizationSettings()
                .compose(applySingleIoSchedulers())
                .subscribe(this::showShotsDetail,
                        throwable -> Timber.e(throwable, "Error getting show details state.")));
    }

    private void showShotsDetail(CustomizationSettings customizationSettings) {
        getView().onDetailsVisibilityChange(customizationSettings.isShowDetails());
    }

    @Override
    public void likeShot(Shot shot) {
        getView().closeFabMenu();
        if (!shot.isLiked()) {
            final Subscription subscription = likeShotController.likeShot(shot)
                    .compose(applyCompletableIoSchedulers())
                    .subscribe(() -> onShotLikeCompleted(shot),
                            throwable -> handleError(throwable, "Error while sending shot like"));
            subscriptions.add(subscription);
        }
    }

    @Override
    public void getShotsFromServer(boolean pullToRefresh) {
        if (refreshSubscription.isUnsubscribed()) {
            loadMoreSubscription.unsubscribe();
            pageNumber = FIRST_PAGE;
            getView().showLoadingIndicator(pullToRefresh);
            refreshSubscription = shotsController.getShots(pageNumber, SHOTS_PER_PAGE)
                    .compose(androidIO())
                    .subscribe(shotList -> {
                        Timber.d("Shots received!");
                        hasMore = shotList.size() >= SHOTS_PER_PAGE;
                        getView().hideLoadingIndicator();
                        getView().setData(shotList);
                        getView().showContent();
                        getView().showFirstShot();
                    }, throwable -> handleError(throwable, "Error while getting shots"));
        }
    }

    @Override
    public void getMoreShotsFromServer() {
        if (hasMore && refreshSubscription.isUnsubscribed() && loadMoreSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreSubscription = shotsController.getShots(pageNumber, SHOTS_PER_PAGE)
                    .compose(androidIO())
                    .subscribe(shotList -> {
                        Timber.d("Shots received!");
                        hasMore = shotList.size() >= SHOTS_PER_PAGE;
                        getView().hideLoadingIndicator();
                        getView().showMoreItems(shotList);
                    }, throwable -> handleError(throwable, "Error while getting more shots"));
        }
    }

    @Override
    public void handleAddShotToBucket(Shot shot) {
        getView().closeFabMenu();
        getView().showBucketChoosing(shot);
    }

    @Override
    public void addShotToBucket(Bucket bucket, Shot shot) {
        subscriptions.add(
                bucketsController.addShotToBucket(bucket.id(), shot)
                        .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                        .subscribe(() -> onShotBucketedCompleted(shot),
                                throwable -> handleError(throwable, "Error while adding shot to bucket"))
        );
    }

    @Override
    public void showShotDetails(Shot shot) {
        getView().showShotDetails(shot);
    }

    @Override
    public void showCommentInput(Shot selectedShot) {
        getView().closeFabMenu();
        getView().showDetailsScreenInCommentMode(selectedShot);
    }

    @Override
    public void handleFollowShotAuthor(Shot shot) {
        getView().closeFabMenu();
        subscriptions.add(followersController.followUser(shot.author())
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                .subscribe(this::onUserFollowCompleted,
                        throwable -> handleError(throwable, "Error while following shot author")));
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().hideLoadingIndicator();
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    @Override
    public void removeShotFromBuckets(List<Bucket> list, Shot shot) {
        for (Bucket bucket : list) {
            subscriptions.add(
                    bucketsController.removeShotFromBucket(bucket.id(), shot)
                            .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                            .subscribe(getView()::showShotRemoveFromBucketSuccess,
                                    throwable -> handleError(throwable, "Error while removing shot from bucket"))
            );
        }
    }

    private void onShotBucketedCompleted(Shot shot) {
        Timber.d("Shots bucketed: %s", shot);
        getView().showBucketAddSuccess();
        rxBus.send(new ShotUpdatedEvent(updateShotBucketedStatus(shot)));
        getView().onShotAddedToBucket();
    }

    private void onShotLikeCompleted(Shot shot) {
        Timber.d("Shot liked : %s", shot);
        rxBus.send(new ShotUpdatedEvent(updateShotLikeStatus(shot)));
        getView().onShotLiked();
    }

    private void onUserFollowCompleted() {
        Timber.d("Followed shot author");
        getView().onUserFollowed();
    }

    private Shot updateShotBucketedStatus(Shot shot) {
        return Shot.update(shot.isLiked() ? shot : updateShotLikeStatus(shot))
                .isBucketed(true)
                .build();
    }

    private Shot updateShotLikeStatus(Shot shot) {
        return Shot.update(shot)
                .likesCount(shot.likesCount() + 1)
                .isLiked(true)
                .build();
    }

    private void setupRxBus() {
        busSubscription = rxBus.getEvents(DetailsVisibilityChangeEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(event -> getView().onDetailsVisibilityChange(event.isDetailsVisible()));
        busUpdateShotSubscription = rxBus.getEvents(ShotUpdatedEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(event -> getView().updateShot(event.getShot()));
    }
}
