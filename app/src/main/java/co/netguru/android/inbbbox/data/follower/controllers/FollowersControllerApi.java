package co.netguru.android.inbbbox.data.follower.controllers;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import retrofit2.adapter.rxjava.HttpException;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class FollowersControllerApi extends BaseFollowersController implements FollowersController {

    private static final int CODE_USER_IS_FOLLOWED = 204;
    private static final int CODE_USER_IS_NOT_FOLLOWED = 404;

    public FollowersControllerApi(FollowersApi followersApi, ShotsApi shotsApi) {
        super(followersApi, shotsApi);
    }

    @Override
    public Observable<UserWithShots> getFollowedUsers(int pageNumber, int pageCount, int followerShotPageCount) {
        return getFollowersFromApi(pageNumber, pageCount, followerShotPageCount);
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
}
