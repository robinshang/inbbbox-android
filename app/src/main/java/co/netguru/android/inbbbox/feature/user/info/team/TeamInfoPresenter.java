package co.netguru.android.inbbbox.feature.user.info.team;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import co.netguru.android.inbbbox.feature.user.UserClickListener;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

@FragmentScope
public class TeamInfoPresenter extends MvpNullObjectBasePresenter<TeamInfoContract.View>
        implements TeamInfoContract.Presenter, UserClickListener {

    private static final int USERS_PAGE_COUNT = 10;
    private static final int SHOTS_PER_USER = 12;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final TeamController teamController;
    private final UserShotsController userShotsController;
    private final ErrorController errorController;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private final User user;
    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextUsersSubscription;

    private int pageNumber = 1;
    private boolean hasMore = true;

    @Inject
    public TeamInfoPresenter(TeamController teamController, UserShotsController userShotsController,
                             ErrorController errorController, User user, RxBus rxBus,
                             BucketsController bucketsController) {
        this.teamController = teamController;
        this.userShotsController = userShotsController;
        this.errorController = errorController;
        this.user = user;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextUsersSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void attachView(TeamInfoContract.View view) {
        super.attachView(view);

        loadTeamData();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        refreshSubscription.unsubscribe();
        loadNextUsersSubscription.unsubscribe();
        subscriptions.clear();
    }

    private void loadTeamData() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextUsersSubscription.unsubscribe();
            pageNumber = 1;

            refreshSubscription = teamController.getTeamMembers(user.id(), pageNumber,
                    USERS_PAGE_COUNT)
                    .flatMapObservable(Observable::from)
                    .flatMap(member -> userShotsController.getUserShotsList(member.id(), 1,
                            SHOTS_PER_USER, true)
                            .flatMap(Observable::from)
                            .map(shot -> Shot.update(shot).author(member).build())
                            .toList()
                            .subscribeOn(Schedulers.io()), UserWithShots::create)
                    .toList()
                    .toSingle()
                    .compose(applySingleIoSchedulers())
                    .subscribe(users -> {
                                getView().showTeamMembers(users);
                                hasMore = users.size() >= USERS_PAGE_COUNT;
                            },
                            throwable -> handleError(throwable, "Error while loading team members"));
        }
    }

    public void handleError(Throwable throwable, String errorText) {
        Timber.d(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    @Override
    public void onUserClick(User user) {
        getView().openUserDetails(user);
    }

    @Override
    public void loadMoreTeamMembers() {
        if (hasMore && refreshSubscription.isUnsubscribed() && loadNextUsersSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextUsersSubscription = teamController.getTeamMembers(user.id(), pageNumber, USERS_PAGE_COUNT)
                    .toObservable()
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreTeamMembersView))
                    .toSingle()
                    .flatMapObservable(Observable::from)
                    .flatMap(member -> userShotsController.getUserShotsList(member.id(), 1,
                            SHOTS_PER_USER, true)
                            .flatMap(Observable::from)
                            .map(shot -> Shot.update(shot).author(member).build())
                            .toList()
                            .subscribeOn(Schedulers.io()), UserWithShots::create)
                    .toList()
                    .toSingle()
                    .compose(applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideLoadingMoreTeamMembersView)
                    .subscribe(users -> {
                                getView().showMoreTeamMembers(users);
                                hasMore = users.size() >= USERS_PAGE_COUNT;
                            },
                            throwable -> handleError(throwable, "Error while loading team members"));
        }
    }

    @Override
    public void onLinkClick(String url) {
        getView().openUrl(url);
    }

    @Override
    public void onShotClick(Shot shot) {
        getView().openShotDetails(shot);
    }
}
