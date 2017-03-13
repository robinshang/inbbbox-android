package co.netguru.android.inbbbox.feature.user.info.singleuser;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

public class UserInfoPresenter extends MvpNullObjectBasePresenter<UserInfoContract.View>
        implements UserInfoContract.Presenter {

    private static final int PAGE = 1;
    private static final int TEAMS_PAGE_COUNT = 10;
    private static final int SHOTS_PAGE_COUNT = 30;

    private final TeamController teamController;
    private final UserShotsController userShotsController;
    private final ErrorController errorController;
    private final User user;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    UserInfoPresenter(User user, TeamController teamController,
                      UserShotsController userShotsController,
                      ErrorController errorController) {
        this.user = user;
        this.teamController = teamController;
        this.userShotsController = userShotsController;
        this.errorController = errorController;
    }

    @Override
    public void attachView(UserInfoContract.View view) {
        super.attachView(view);
        getUserTeams();
        getUserShots();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void onTeamClick(User user) {
        getView().openTeam(user);
    }

    @Override
    public void onShotClick(Shot shot) {
        getView().openShot(shot);
    }

    @Override
    public void onLinkClick(String url) {
        getView().openUrl(url);
    }

    private void getUserTeams() {
        subscriptions.add(teamController.getUserTeams(user.id(), PAGE, TEAMS_PAGE_COUNT)
                .compose(applySingleIoSchedulers())
                .subscribe(getView()::showTeams,
                        throwable -> handleError(throwable, "Error while loading team members")));
    }

    private void getUserShots() {
        subscriptions.add(userShotsController.getUserShotsList(user.id(),
                PAGE, SHOTS_PAGE_COUNT)
                .compose(androidIO())
                .compose(fromListObservable())
                .map(shot -> Shot.update(shot).author(user).build())
                .toSortedList(Shot::compareShotByPopularity)
                .subscribe(getView()::showShots,
                        throwable -> handleError(throwable, "Error while refreshing user shots")));
    }

    private void handleError(Throwable throwable, String errorText) {
        Timber.d(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }
}
