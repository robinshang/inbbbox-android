package co.netguru.android.inbbbox.data.follower.controllers;

import java.util.List;

import co.netguru.android.inbbbox.data.Cache;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;
import rx.schedulers.Schedulers;

public abstract class BaseFollowersController {

    protected static final int FIRST_PAGE = 1;

    protected final FollowersApi followersApi;
    private final ShotsApi shotsApi;
    private final Cache<UserWithShots> userWithShotsCache = new Cache<>();

    protected BaseFollowersController(FollowersApi followersApi, ShotsApi shotsApi) {
        this.followersApi = followersApi;
        this.shotsApi = shotsApi;
    }

    protected Observable<UserWithShots> getFollowersFromApi(int pageNumber, int pageCount, int followerShotPageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(followerEntity -> User.create(followerEntity.user()))
                .flatMap(user -> fetchAndCacheUserWithShots(user, followerShotPageCount));
    }

    protected Observable<UserWithShots> getFollowerWithShots(User user, int followerShotsPageCount) {
        return shotsApi.getUserShots(user.id(), FIRST_PAGE, followerShotsPageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .map(shot -> Shot.update(shot).author(user).build())
                .toList()
                .map(shotList -> cacheUserWithShots(user, shotList))
                .subscribeOn(Schedulers.io());
    }

    private Observable<UserWithShots> fetchAndCacheUserWithShots(User user, int followerShotPageCount) {
        return Observable.just(userWithShotsCache.get(user.id()))
                .filter(val -> val != null)
                .switchIfEmpty(getFollowerWithShots(user, followerShotPageCount));
    }

    private UserWithShots cacheUserWithShots(User user, List<Shot> shotList) {
        UserWithShots userWithShots = UserWithShots.create(user, shotList);
        userWithShotsCache.add(userWithShots);
        return userWithShots;
    }

}
