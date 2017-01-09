package co.netguru.android.inbbbox.data.dribbbleuser.team;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import rx.Observable;
import rx.Single;

@Singleton
public class TeamControllerApi implements TeamController {

    private TeamApi teamApi;
    private UserShotsController userShotsController;

    @Inject
    public TeamControllerApi(TeamApi teamApi, UserShotsController userShotsController) {
        this.teamApi = teamApi;
        this.userShotsController = userShotsController;
    }

    public Single<List<UserWithShots>> getTeamMembers(long teamId, int pageNumber,
                                                      int pageCount, int shotsPerUser) {
        return teamApi.getTeamMembers(teamId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .map(User::create)
                .flatMap(userEntity -> userShotsController.getUserShotsList(userEntity.id(), 1, shotsPerUser),
                        UserWithShots::create)
                .toList()
                .toSingle();
    }
}