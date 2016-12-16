package co.netguru.android.inbbbox.controler.followers;

import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;


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
}
