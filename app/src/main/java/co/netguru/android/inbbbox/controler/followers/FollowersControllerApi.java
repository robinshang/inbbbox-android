package co.netguru.android.inbbbox.controler.followers;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class FollowersControllerApi implements FollowersController {

    private final FollowersApi followersApi;

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
    public Single<Boolean> isUserFollowed(long id) {
        return followersApi.checkIfUserIsFollowed(id)
                .map(response -> isUserFollowedDependingOnCode(response.code()));
    }

    private boolean isUserFollowedDependingOnCode(int code) {
        boolean userIsFollowed = false;
        if (code == Constants.ApiCodes.CODE_USER_IS_FOLLOWED) {
            userIsFollowed = true;

        } else if (code == Constants.ApiCodes.CODE_USER_IS_NOT_FOLLOWED) {
            userIsFollowed = false;
        }

        return userIsFollowed;
    }
}
