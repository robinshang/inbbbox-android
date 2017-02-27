package co.netguru.android.inbbbox.feature.user.info.team;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.user.UserClickListener;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

@FragmentScope
public class TeamInfoPresenter extends MvpNullObjectBasePresenter<TeamInfoContract.View>
        implements TeamInfoContract.Presenter, UserClickListener {

    private static final int USERS_PAGE_COUNT = 15;
    private static final int SHOTS_PER_USER = 12;

    private final TeamController teamController;
    private final UserShotsController userShotsController;

    private final User user;
    @NonNull
    private Subscription refreshSubscription;
    @NonNull
    private Subscription loadNextUsersSubscription;

    private int pageNumber = 1;
    private boolean hasMore = true;

    @Inject
    public TeamInfoPresenter(TeamController teamController, UserShotsController userShotsController,
                             User user) {
        this.teamController = teamController;
        this.userShotsController = userShotsController;
        this.user = user;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextUsersSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void attachView(TeamInfoContract.View view) {
        super.attachView(view);

        loadTeamData();
    }

    private void loadTeamData() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextUsersSubscription.unsubscribe();
            pageNumber = 1;

            refreshSubscription = teamController.getTeamMembers(user.id(), pageNumber,
                    USERS_PAGE_COUNT)
                    .flatMapObservable(Observable::from)
                    .flatMap(member -> userShotsController.getUserShotsList(member.id(), 1, SHOTS_PER_USER)
                            .flatMap(Observable::from)
                            .map(shot -> Shot.update(shot).author(member).build())
                            .toList()
                            .subscribeOn(Schedulers.io()), UserWithShots::create)
                    .toList()
                    .toSingle()
                    .compose(applySingleIoSchedulers())
                    .subscribe(users -> {
                                getView().showData(users);
                                hasMore = users.size() >= USERS_PAGE_COUNT;
                                Timber.d("fetched users: " + users.size());
                            },
                            throwable -> handleError(throwable, "Error while loading team members"));
        }
    }

    public void handleError(Throwable throwable, String errorText) {
        Timber.d(throwable, errorText);
//        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    @Override
    public void onUserClick(User user) {
        getView().openUserDetails(user);
    }

    @Override
    public void onShotClick(Shot shot) {
        getView().openShotDetails(shot);
    }
}
