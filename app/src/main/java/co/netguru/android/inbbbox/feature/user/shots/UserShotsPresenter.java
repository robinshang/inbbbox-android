package co.netguru.android.inbbbox.feature.user.shots;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Collections;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

@FragmentScope
public class UserShotsPresenter extends MvpNullObjectBasePresenter<UserShotsContract.View>
        implements UserShotsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;

    private final UserShotsController userShotsController;
    private final BucketsController bucketsController;
    private final RxBus rxBus;

    private final ErrorController errorController;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    @NonNull
    private Subscription loadMoreShotsSubscription;

    @NonNull
    private Subscription refreshShotsSubscription;

    private User user;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    UserShotsPresenter(UserShotsController userShotsController,
                       ErrorController errorController, @NonNull User user,
                       BucketsController bucketsController, RxBus rxBus) {
        this.userShotsController = userShotsController;
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        this.rxBus = rxBus;
        refreshShotsSubscription = Subscriptions.unsubscribed();
        loadMoreShotsSubscription = Subscriptions.unsubscribed();
        this.user = user;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            loadMoreShotsSubscription.unsubscribe();
            refreshShotsSubscription.unsubscribe();
            subscriptions.clear();
        }
    }

    @Override
    public void userDataReceived(User user) {
        if (user != null) {
            this.user = user;
            refreshUserShots();
        }
    }

    @Override
    public void refreshUserShots() {
        if (refreshShotsSubscription.isUnsubscribed()) {
            loadMoreShotsSubscription.unsubscribe();
            pageNumber = 1;
            loadMoreShotsSubscription = userShotsController.getUserShotsList(user.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .compose(fromListObservable())
                    .map(shot -> Shot.update(shot).author(user).build())
                    .toList()
                    .doAfterTerminate(getView()::hideProgress)
                    .subscribe(shotList -> {
                        hasMore = shotList.size() == SHOT_PAGE_COUNT;
                        getView().setData(shotList);
                    }, throwable -> handleError(throwable, "Error while refreshing user shots"));
        }
    }

    @Override
    public void getMoreUserShotsFromServer() {
        if (hasMore && refreshShotsSubscription.isUnsubscribed() && loadMoreShotsSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreShotsSubscription = userShotsController.getUserShotsList(user.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(fromListObservable())
                    .map(shot -> Shot.update(shot).author(user).build())
                    .toList()
                    .compose(androidIO())
                    .subscribe(shotList -> {
                        hasMore = shotList.size() == SHOT_PAGE_COUNT;
                        getView().showMoreUserShots(shotList);
                    }, throwable -> handleError(throwable, "Error while getting more user shots"));
        }
    }

    @Override
    public void showShotDetails(Shot shot) {
        getView().openShotDetailsScreen(shot, Collections.emptyList(), user.id());
    }

    @Override
    public void onBucketShot(Shot shot) {
        getView().showBucketChooserView(shot);
    }

    @Override
    public void addShotToBucket(Shot shot, Bucket bucket) {
        subscriptions.add(
                bucketsController.addShotToBucket(bucket.id(), shot)
                        .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                        .subscribe(() -> onShotBucketedCompleted(shot),
                                throwable -> handleError(throwable, "Error while adding shot to bucket"))
        );
    }

    private void onShotBucketedCompleted(Shot shot) {
        getView().showBucketAddSuccess();
        rxBus.send(new ShotUpdatedEvent(updateShotBucketedStatus(shot)));
    }

    private Shot updateShotBucketedStatus(Shot shot) {
        return Shot.update(shot)
                .isBucketed(true)
                .build();
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }
}
