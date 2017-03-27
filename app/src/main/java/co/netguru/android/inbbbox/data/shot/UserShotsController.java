package co.netguru.android.inbbbox.data.shot;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.cache.CacheStrategy;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;

import static co.netguru.android.inbbbox.data.dribbbleuser.user.User.TYPE_TEAM;

@Singleton
public class UserShotsController {

    private final ShotsApi shotsApi;

    @Inject
    public UserShotsController(ShotsApi shotsApi) {
        this.shotsApi = shotsApi;
    }

    public Observable<List<Shot>> getUserOrTeamShots(User user, int pageNumber,
                                                     int pageCount,
                                                     boolean canUseCache) {
        if (user.type().equals(TYPE_TEAM)) {
            return getTeamShotsList(user.id(), pageNumber, pageCount, canUseCache);
        } else {
            return getUserShotsList(user.id(), pageNumber, pageCount, canUseCache);
        }
    }

    public Observable<UserWithShots> getTeamUserWithShots(Team team, int pageNumber, int pageCount,
                                                          boolean canUseCacheForShots) {
        return getTeamShotsList(team.id(), pageNumber, pageCount, canUseCacheForShots)
                .flatMap(Observable::from)
                .map(shot -> Shot.update(shot).author(User.createFromTeam(team)).build())
                .toList()
                .map(shotList -> UserWithShots.create(User.createFromTeam(team), shotList));
    }

    public Observable<List<Shot>> getUserShotsList(long userId, int pageNumber, int pageCount,
                                                   boolean canUseCache) {
        return shotsApi.getUserShots(userId, pageNumber, pageCount,
                canUseCache ? CacheStrategy.longCache() : CacheStrategy.noCache())
                .compose(createShots());
    }

    private Observable<List<Shot>> getTeamShotsList(long teamId, int pageNumber, int pageCount,
                                                    boolean canUseCacheForShots) {
        return shotsApi.getTeamShots(teamId, pageNumber, pageCount,
                canUseCacheForShots ? CacheStrategy.longCache() : CacheStrategy.noCache())
                .compose(createShots());
    }

    private Observable.Transformer<List<ShotEntity>, List<Shot>> createShots() {
        return observable -> observable.flatMap(Observable::from)
                .map(Shot::create)
                .toList();
    }
}
