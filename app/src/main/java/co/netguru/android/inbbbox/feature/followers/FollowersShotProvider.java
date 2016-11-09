package co.netguru.android.inbbbox.feature.followers;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;

import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.model.ui.Follower;

import rx.Observable;

@FragmentScope
public class FollowersShotProvider {

    private final ShotsApi shotsApi;

    @Inject
    public FollowersShotProvider(ShotsApi shotsApi) {
        this.shotsApi = shotsApi;
    }

    public Observable<Follower> getFollowedUserWithShots(Follower follower) {
      return null;
//        shotsApi.getFollowedUserShots(follower.id())
//              .flatMap(Observable::from)
//              .map(shotsMapper::getShot)
//              .toList()
//              .map(shotList -> createFollowerWithShots(shotList, follower));
    }

//    private Follower createFollowerWithShots(List<Shot> shotList, Follower follower) {
//        return Follower.builder()
//                .id(follower.id())
//                .shotsCount(follower.shotsCount())
//                .avatarUrl(follower.avatarUrl())
//                .username(follower.username())
//                .name(follower.name())
//                .shotList(shotList)
//                .build();
//    }
}
