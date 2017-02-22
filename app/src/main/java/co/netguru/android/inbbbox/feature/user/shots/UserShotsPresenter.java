package co.netguru.android.inbbbox.feature.user.shots;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

@FragmentScope
public class UserShotsPresenter extends MvpNullObjectBasePresenter<UserShotsContract.View>
        implements UserShotsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;

    private final UserShotsController userShotsController;
    private final ErrorController errorController;
    @NonNull
    private Subscription loadMoreShotsSubscription;

    @NonNull
    private Subscription refreshShotsSubscription;

    private UserWithShots userWithShots;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    UserShotsPresenter(UserShotsController userShotsController,
                       ErrorController errorController, @NonNull UserWithShots userWithShots) {
        this.userShotsController = userShotsController;
        this.errorController = errorController;
        refreshShotsSubscription = Subscriptions.unsubscribed();
        loadMoreShotsSubscription = Subscriptions.unsubscribed();
        this.userWithShots = userWithShots;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            loadMoreShotsSubscription.unsubscribe();
            refreshShotsSubscription.unsubscribe();
        }
    }

    @Override
    public void userDataReceived(UserWithShots userWithShots) {
        if (userWithShots != null) {
            this.userWithShots = userWithShots;
            refreshUserShots();
        }
    }

    @Override
    public void refreshUserShots() {
        if (refreshShotsSubscription.isUnsubscribed()) {
            loadMoreShotsSubscription.unsubscribe();
            pageNumber = 1;
            loadMoreShotsSubscription = userShotsController.getUserShotsList(userWithShots.user().id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .compose(fromListObservable())
                    .map(shot -> Shot.update(shot).author(userWithShots.user()).build())
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
            loadMoreShotsSubscription = userShotsController.getUserShotsList(userWithShots.user().id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(fromListObservable())
                    .map(shot -> Shot.update(shot).author(userWithShots.user()).build())
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
        getView().openShotDetailsScreen(shot, userWithShots.shotList(), userWithShots.user().id());
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }
}
