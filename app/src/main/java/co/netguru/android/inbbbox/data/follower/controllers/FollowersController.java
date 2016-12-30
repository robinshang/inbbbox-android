package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FollowersController {

    Observable<User> getFollowedUsers(int pageNumber, int pageCount, int followerShotPageCount);

    Completable unFollowUser(long id);

    Completable followUser(User follower);

    Single<Boolean> isUserFollowed(long id);
}
