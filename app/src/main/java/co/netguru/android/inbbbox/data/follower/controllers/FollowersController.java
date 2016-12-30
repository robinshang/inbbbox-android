package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FollowersController {

    Observable<FollowerEntity> getFollowedUsers(int pageNumber, int pageCount);

    Completable unFollowUser(long id);

    Completable followUser(long id);

    Single<Boolean> isUserFollowed(long id);
}
