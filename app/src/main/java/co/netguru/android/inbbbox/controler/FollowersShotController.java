package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Observable;

@Singleton
public class FollowersShotController {

    private final ShotsApi shotsApi;

    @Inject
    public FollowersShotController(ShotsApi shotsApi) {
        this.shotsApi = shotsApi;
    }

    public Observable<Follower> getFollowedUserWithShots(FollowerEntity follower) {
       return shotsApi.getFollowedUserShots(follower.user().id())
              .flatMap(Observable::from)
              .map(Shot::create)
              .toList()
              .map(shotList -> Follower.create(follower, shotList));
    }
}
