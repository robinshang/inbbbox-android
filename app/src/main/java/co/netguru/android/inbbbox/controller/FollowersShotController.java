package co.netguru.android.inbbbox.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.User;
import rx.Observable;

@Singleton
public class FollowersShotController {

    private final ShotsApi shotsApi;

    @Inject
    public FollowersShotController(ShotsApi shotsApi) {
        this.shotsApi = shotsApi;
    }

    public Observable<Follower> getFollowedUserWithShots(FollowerEntity follower, int pageNumber, int pageCount) {
        return shotsApi.getUserShots(follower.user().id(), pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .map(shot -> Shot.update(shot).author(User.create(follower.user())).build())
                .toList()
                .map(shotList -> Follower.create(follower, shotList));
    }
}
