package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import retrofit2.Response;
import rx.Completable;
import rx.Observable;

@Singleton
public class FollowersController {

    private final FollowersApi followersApi;

    @Inject
    public FollowersController(FollowersApi followersApi) {
        this.followersApi = followersApi;
    }

    public Observable<FollowerEntity> getFollowedUsers(int pageNumber, int pageCount) {
        return followersApi.getFollowedUsers(pageNumber, pageCount)
                .flatMap(Observable::from);
    }

    public Completable unFollowUser(long id) {
        return followersApi.unFollowUser(id);
    }

    public Completable followUser(long id) {
        return followersApi.followUser(id);
    }

    public Observable<Response<Completable>> checkIfUserIsFollowed(long id) {
        return followersApi.checkIfUserIsFollowed(id);
    }
}
