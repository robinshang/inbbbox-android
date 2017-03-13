package co.netguru.android.inbbbox.data.follower.controllers;

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
    private final Cache<UserWithShots> userWithShotsCache;

    protected BaseFollowersController(FollowersApi followersApi, ShotsApi shotsApi) {
        this.followersApi = followersApi;
        this.shotsApi = shotsApi;
        userWithShotsCache = new Cache<>();
    }

    protected Observable<UserWithShots> getFollowersFromApi(int pageNumber, int pageCount, int followerShotPageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(followerEntity -> User.create(followerEntity.user()))
                .flatMap(user -> {
                    UserWithShots userWithShots = userWithShotsCache.get(user.id());
                    if (userWithShots != null) {
                        return Observable.just(userWithShots);
                    } else
                        return getFollowerWithShots(user, followerShotPageCount);
                });
    }

    protected Observable<UserWithShots> getFollowerWithShots(User user, int followerShotsPageCount) {
        return shotsApi.getUserShots(user.id(), FIRST_PAGE, followerShotsPageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .map(shot -> Shot.update(shot).author(user).build())
                .toList()
                .map(shotList -> {
                    UserWithShots userWithShots = UserWithShots.create(user, shotList);
                    userWithShotsCache.add(userWithShots);
                    return userWithShots;
                })
                .subscribeOn(Schedulers.io());
    }

}
