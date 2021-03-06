package co.netguru.android.inbbbox.data.dribbbleuser.team;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import rx.Observable;
import rx.Single;

@Singleton
public final class TeamControllerApi implements TeamController {

    private TeamApi teamApi;

    @Inject
    public TeamControllerApi(TeamApi teamApi) {
        this.teamApi = teamApi;
    }

    @Override
    public Single<List<User>> getTeamMembers(long teamId, int pageNumber,
                                             int pageCount) {
        return teamApi.getTeamMembers(teamId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .map(User::create)
                .toList()
                .toSingle();
    }

    @Override
    public Single<List<User>> getUserTeams(long userId, int pageNumber, int pageCount) {
        return teamApi.getUserTeams(userId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .map(User::create)
                .toList()
                .toSingle();
    }
}