package co.netguru.android.inbbbox.data.follower.controllers;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.shot.ShotsApi;
import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import co.netguru.android.inbbbox.data.follower.model.ui.Follower;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
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
