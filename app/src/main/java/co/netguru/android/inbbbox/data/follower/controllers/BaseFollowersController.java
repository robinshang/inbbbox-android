package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.cache.CacheStrategy;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;
import rx.schedulers.Schedulers;

abstract class BaseFollowersController {

    static final int FIRST_PAGE = 1;

    final FollowersApi followersApi;
    private final ShotsApi shotsApi;

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
                .flatMap(user -> getFollowerWithShots(user, followerShotPageCount, canUseCacheForShots));
    }

    Observable<UserWithShots> getFollowerWithShots(User user, int followerShotsPageCount,
                                                   boolean canUseCacheForShots) {
        return shotsApi.getUserShots(user.id(), FIRST_PAGE, followerShotsPageCount,
                canUseCacheForShots ? CacheStrategy.longCache() : CacheStrategy.noCache())
                .flatMap(Observable::from)
                .map(Shot::create)
                .map(shot -> Shot.update(shot).author(user).build())
                .toList()
                .map(shotList -> UserWithShots.create(user, shotList))
                .subscribeOn(Schedulers.io());
    }

}
