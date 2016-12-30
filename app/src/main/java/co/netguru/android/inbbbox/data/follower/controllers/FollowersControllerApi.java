package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import retrofit2.adapter.rxjava.HttpException;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class FollowersControllerApi implements FollowersController {

    private final FollowersApi followersApi;

    private static final int CODE_USER_IS_FOLLOWED = 204;
    private static final int CODE_USER_IS_NOT_FOLLOWED = 404;

    public FollowersControllerApi(FollowersApi followersApi) {
        this.followersApi = followersApi;
    }

    @Override
    public Observable<FollowerEntity> getFollowedUsers(int pageNumber, int pageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from);
    }

    @Override
    public Completable unFollowUser(long id) {
        return followersApi.unFollowUser(id);
    }

    @Override
    public Completable followUser(long id) {
        return followersApi.followUser(id);
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
}
