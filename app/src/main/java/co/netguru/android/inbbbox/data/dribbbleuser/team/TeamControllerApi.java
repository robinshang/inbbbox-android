package co.netguru.android.inbbbox.data.dribbbleuser.team;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import rx.Observable;
import rx.Single;

@Singleton
public class TeamControllerApi implements TeamController {

    private TeamApi teamApi;

    @Inject
    public TeamControllerApi(TeamApi teamApi) {
        this.teamApi = teamApi;
    }

    public Single<List<User>> getTeamMembers(long teamId, int pageNumber,
                                             int pageCount, int shotsPerUser) {
        return teamApi.getTeamMembers(teamId, pageNumber, pageCount)
                .flatMapObservable(Observable::from)
                .map(User::create)
                .toList()
                .toSingle();
    }
}