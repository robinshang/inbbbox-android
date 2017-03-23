package co.netguru.android.inbbbox.data.shot;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.cache.CacheStrategy;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

@Singleton
public class UserShotsController {

    private final ShotsApi shotsApi;

    @Inject
    public UserShotsController(ShotsApi shotsApi) {
        this.shotsApi = shotsApi;
    }

    public Observable<List<Shot>> getUserShotsList(long id, int pageNumber, int pageCount,
                                                   boolean canUseCache) {
        return shotsApi.getUserShots(id, pageNumber, pageCount,
                canUseCache ? CacheStrategy.longCache() : CacheStrategy.noCache())
                .flatMap(Observable::from)
                .map(Shot::create)
                .toList();
    }

    public Observable<UserWithShots> getTeamUserWithShots(Team team, int pageNumber, int pageCount,
                                                          boolean canUseCacheForShots) {
        return getUserShotsList(team.id(), pageNumber, pageCount, canUseCacheForShots)
                .compose(fromListObservable())
                .map(shot -> Shot.update(shot).author(User.createFromTeam(team)).build())
                .toList()
                .map(shotList -> UserWithShots.create(User.createFromTeam(team), shotList));
    }
}
