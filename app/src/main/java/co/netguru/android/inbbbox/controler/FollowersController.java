package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.model.ui.Follower;
import rx.Observable;

@Singleton
public class FollowersController {

    private final FollowersApi followersApi;

    @Inject
    public FollowersController(FollowersApi followersApi) {
        this.followersApi = followersApi;
    }

    public Observable<Follower> getFollowedUsers() {
        return followersApi.getFollowedUsers()
                .flatMap(Observable::from)
                .map(Follower::create);
    }
}
