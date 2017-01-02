package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import retrofit2.adapter.rxjava.HttpException;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class FollowersControllerApi implements FollowersController {

    private static final int CODE_USER_IS_FOLLOWED = 204;
    private static final int CODE_USER_IS_NOT_FOLLOWED = 404;
    private static final int FIRST_PAGE = 1;

    private final FollowersApi followersApi;
    private final ShotsApi shotsApi;

    public FollowersControllerApi(FollowersApi followersApi, ShotsApi shotsApi) {
        this.followersApi = followersApi;
        this.shotsApi = shotsApi;
    }

    @Override
    public Observable<UserWithShots> getFollowedUsers(int pageNumber, int pageCount, int followerShotPageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from)
                .flatMap(follower -> getFollowerWithShots(follower, followerShotPageCount));
    }

    @Override
    public Completable unFollowUser(long id) {
        return followersApi.unFollowUser(id);
    }

    @Override
    public Completable followUser(User user) {
        return followersApi.followUser(user.id());
    }

    @Override
    public Single<Boolean> isUserFollowed(long userId) {
        return followersApi.checkIfUserIsFollowed(userId)
                .flatMap(voidResponse -> {
                    switch (voidResponse.code()) {
                        case CODE_USER_IS_FOLLOWED:
                            return Single.just(true);
                        case CODE_USER_IS_NOT_FOLLOWED:
                            return Single.just(false);
                        default:
                            return Single.error(new HttpException(voidResponse));
                    }
                });
    }

    private Observable<UserWithShots> getFollowerWithShots(FollowerEntity followerEntity, int followerShotsPageCount) {
        return shotsApi.getUserShots(followerEntity.user().id(), FIRST_PAGE, followerShotsPageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .map(shot -> Shot.update(shot).author(User.create(followerEntity.user())).build())
                .toList()
                .map(shotList -> UserWithShots.create(User.create(followerEntity.user()), shotList));
    }
}
