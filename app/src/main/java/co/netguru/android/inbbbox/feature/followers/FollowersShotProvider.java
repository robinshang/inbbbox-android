package co.netguru.android.inbbbox.feature.followers;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.ui.Follower;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.feature.shots.ShotsMapper;
import rx.Observable;

@FragmentScope
public class FollowersShotProvider {

    private final ShotsMapper shotsMapper;
    private final ShotsApi shotsApi;

    @Inject
    public FollowersShotProvider(ShotsMapper shotsMapper, ShotsApi shotsApi) {
        this.shotsMapper = shotsMapper;
        this.shotsApi = shotsApi;
    }

    public Observable<Follower> getFollowedUserWithShots(Follower follower) {
      return shotsApi.getFollowedUserShots(follower.id())
              .flatMap(Observable::from)
              .map(shotsMapper::getShot)
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
