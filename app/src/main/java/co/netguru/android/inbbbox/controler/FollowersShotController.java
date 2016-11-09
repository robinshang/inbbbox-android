package co.netguru.android.inbbbox.controler;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.ShotsApi;
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

    public Observable<Follower> getFollowedUserWithShots(Follower follower) {
       return shotsApi.getFollowedUserShots(follower.id())
              .flatMap(Observable::from)
              .map(Shot::create)
              .toList()
              .map(shotList -> createFollowerWithShots(shotList, follower));
    }

    private Follower createFollowerWithShots(List<Shot> shotList, Follower follower) {
        return Follower.builder()
                .id(follower.id())
                .shotsCount(follower.shotsCount())
                .avatarUrl(follower.avatarUrl())
                .username(follower.username())
                .name(follower.name())
                .shotList(shotList)
                .build();
    }
}
