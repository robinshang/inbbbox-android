package co.netguru.android.inbbbox.data.follower.controllers;

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

    protected BaseFollowersController(FollowersApi followersApi, ShotsApi shotsApi) {
        this.followersApi = followersApi;
        this.shotsApi = shotsApi;
    }

    protected Observable<UserWithShots> getFollowersFromApi(int pageNumber, int pageCount, int followerShotPageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(followerEntity -> User.create(followerEntity.user()))
                .flatMap(user -> getFollowerWithShots(user, followerShotPageCount));
    }

    protected Observable<UserWithShots> getFollowerWithShots(User user, int followerShotsPageCount) {
        return shotsApi.getUserShots(user.id(), FIRST_PAGE, followerShotsPageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .map(shot -> Shot.update(shot).author(user).build())
                .toList()
                .map(shotList -> UserWithShots.create(user, shotList))
                .subscribeOn(Schedulers.io());
    }

}
