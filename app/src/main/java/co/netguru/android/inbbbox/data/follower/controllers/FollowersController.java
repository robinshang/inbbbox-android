package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FollowersController {

    Observable<UserWithShots> getFollowedUsers(int pageNumber, int pageCount, int followerShotPageCount);

    Completable unFollowUser(long id);

    Completable followUser(User user);

    Single<Boolean> isUserFollowed(long id);
}
