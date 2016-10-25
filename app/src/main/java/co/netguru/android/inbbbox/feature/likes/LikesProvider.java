package co.netguru.android.inbbbox.feature.likes;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.api.LikesApi;
import co.netguru.android.inbbbox.data.ui.LikedShot;
import co.netguru.android.inbbbox.di.scope.FragmentScope;
import rx.Observable;

@FragmentScope
public final class LikesProvider {

    private final LikesApi likesApi;
    private final LikedShotsMapper likedShotsMapper;

    @Inject
    LikesProvider(LikesApi likesApi, LikedShotsMapper likedShotsMapper) {
        this.likesApi = likesApi;
        this.likedShotsMapper = likedShotsMapper;
    }

    public Observable<LikedShot> getLikedShots() {
        return likesApi.getLikedShots()
                .flatMap(Observable::from)
                .map(likedShotsMapper::toLikedShot);
    }
}
