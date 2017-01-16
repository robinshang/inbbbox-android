package co.netguru.android.inbbbox.feature.team;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamController;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applyCompletableIoSchedulers;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

@FragmentScope
public class TeamDetailsPresenter extends MvpNullObjectBasePresenter<TeamDetailsContract.View>
        implements TeamDetailsContract.Presenter {

    private static final int USERS_PAGE_COUNT = 15;
    private static final int SHOTS_PER_USER = 4;

    private final TeamController teamController;
    private final FollowersController followersController;
    private final ErrorController errorController;
    private final UserShotsController userShotsController;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextUsersSubscription;
    private boolean hasMore = true;
    private int pageNumber = 1;
    private UserWithShots team;

    @Inject
    public TeamDetailsPresenter(TeamController teamController,
                                FollowersController followersController, ErrorController errorController,
                                UserShotsController userShotsController) {
        this.teamController = teamController;
        this.followersController = followersController;
        this.errorController = errorController;
        this.userShotsController = userShotsController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextUsersSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadNextUsersSubscription.unsubscribe();
            subscriptions.clear();
        }
    }

    @Override
    public void loadTeamData(UserWithShots team) {
        this.team = team;

        if (refreshSubscription.isUnsubscribed()) {
            loadNextUsersSubscription.unsubscribe();
            pageNumber = 1;

            refreshSubscription = teamController.getTeamMembers(team.user().id(), pageNumber,
                    USERS_PAGE_COUNT)
                    .flatMapObservable(Observable::from)
                    .flatMap(user -> userShotsController.getUserShotsList(user.id(), 1, SHOTS_PER_USER)
                            .flatMap(Observable::from)
                            .map(shot -> Shot.update(shot).author(user).build())
                            .toList()
                            .subscribeOn(Schedulers.io()), UserWithShots::create)
                    .toList()
                    .toSingle()
                    .compose(applySingleIoSchedulers())
                    .doAfterTerminate(getView()::hideProgressBars)
                    .subscribe(users -> {
                                hasMore = users.size() >= USERS_PAGE_COUNT;
                                getView().setData(users);
                                getView().showContent();
                            },
                            throwable -> handleError(throwable, "Error while loading team members"));
        }
    }

    @Override
    public void getMoreMembers(UserWithShots team) {
        if (hasMore && refreshSubscription.isUnsubscribed() && loadNextUsersSubscription.isUnsubscribed()) {
            pageNumber++;
            loadNextUsersSubscription =
                    teamController.getTeamMembers(team.user().id(), pageNumber,
                            USERS_PAGE_COUNT)
                            .flatMapObservable(Observable::from)
                            .flatMap(user -> userShotsController.getUserShotsList(user.id(), 1, SHOTS_PER_USER)
                                    .flatMap(Observable::from)
                                    .map(shot -> Shot.update(shot).author(user).build())
                                    .toList()
                                    .subscribeOn(Schedulers.io()), UserWithShots::create)
                            .toList()
                            .toSingle()
                            .compose(applySingleIoSchedulers())
                            .doAfterTerminate(getView()::hideProgressBars)
                            .subscribe(users -> {
                                hasMore = users.size() >= USERS_PAGE_COUNT;
                                getView().showMoreUsers(users);
                            }, throwable -> handleError(throwable, "Error while loading team members"));
        }
    }

    @Override
    public void checkIfTeamIsFollowed(UserWithShots team) {
        subscriptions.add(
                followersController.isUserFollowed(team.user().id())
                        .compose(applySingleIoSchedulers())
                        .subscribe(getView()::setFollowingMenuIcon,
                                throwable -> handleError(throwable, "Error while checking if user is followed")));
    }


    @Override
    public void onUnfollowClick() {
        getView().showUnfollowDialog(team.user().name());
    }

    @Override
    public void onFollowClick() {
        subscriptions.add(
                followersController.followUser(team.user())
                        .compose(applyCompletableIoSchedulers())
                        .subscribe(() -> getView().setFollowingMenuIcon(true),
                                throwable -> handleError(throwable, "Error while unFollow user")));
    }

    @Override
    public void unfollowUser() {
        subscriptions.add(
                followersController.unFollowUser(team.user().id())
                        .compose(applyCompletableIoSchedulers())
                        .subscribe(() -> getView().setFollowingMenuIcon(false),
                                throwable -> handleError(throwable, "Error while unFollow user")));
    }

    public void handleError(Throwable throwable, String errorText) {
        Timber.d(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }
}