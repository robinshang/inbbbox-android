package co.netguru.android.inbbbox.data.follower.controllers;

import java.util.List;

import co.netguru.android.inbbbox.data.Cache;
import co.netguru.android.inbbbox.data.cache.CacheStrategy;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;
import rx.schedulers.Schedulers;

abstract class BaseFollowersController {

    static final int FIRST_PAGE = 1;

    final FollowersApi followersApi;
    private final ShotsApi shotsApi;
    private final Cache<UserWithShots> userWithShotsCache = new Cache<>();

    BaseFollowersController(FollowersApi followersApi, ShotsApi shotsApi) {
        this.followersApi = followersApi;
        this.shotsApi = shotsApi;
    }

    Observable<UserWithShots> getFollowersFromApi(int pageNumber, int pageCount,
                                                  int followerShotPageCount,
                                                  boolean canUseCacheForShots) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(followerEntity -> User.create(followerEntity.user()))
                .flatMap(user -> fetchAndCacheUserWithShots(user, followerShotPageCount,
                        canUseCacheForShots));
    }

    Observable<UserWithShots> getFollowerWithShots(User user, int followerShotsPageCount,
                                                   boolean canUseCacheForShots) {
        return shotsApi.getUserShots(user.id(), FIRST_PAGE, followerShotsPageCount,
                canUseCacheForShots ? CacheStrategy.longCache() : CacheStrategy.noCache())
                .flatMap(Observable::from)
                .map(Shot::create)
                .map(shot -> Shot.update(shot).author(user).build())
                .toList()
                .map(shotList -> cacheUserWithShots(user, shotList))
                .subscribeOn(Schedulers.io());
    }

    private Observable<UserWithShots> fetchAndCacheUserWithShots(User user, int followerShotPageCount,
                                                                 boolean shouldUseCacheForShots) {
        return Observable.just(userWithShotsCache.get(user.id()))
                .filter(val -> val != null)
                .switchIfEmpty(getFollowerWithShots(user, followerShotPageCount, shouldUseCacheForShots));
    }

    private UserWithShots cacheUserWithShots(User user, List<Shot> shotList) {
        UserWithShots userWithShots = UserWithShots.create(user, shotList);
        userWithShotsCache.add(userWithShots);
        return userWithShots;
    }

}
