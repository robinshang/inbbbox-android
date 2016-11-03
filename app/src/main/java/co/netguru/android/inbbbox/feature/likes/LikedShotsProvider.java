package co.netguru.android.inbbbox.feature.likes;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.api.LikesApi;
import co.netguru.android.inbbbox.data.ui.LikedShot;
import rx.Observable;

@FragmentScope
public class LikedShotsProvider {

    private final LikesApi likesApi;
    private final LikedShotsMapper likedShotsMapper;

    @Inject
    LikedShotsProvider(LikesApi likesApi, LikedShotsMapper likedShotsMapper) {
        this.likesApi = likesApi;
        this.likedShotsMapper = likedShotsMapper;
    }

    public Observable<LikedShot> getLikedShots() {
        return likesApi.getLikedShots()
                .flatMap(Observable::from)
                .map(likedShotsMapper::toLikedShot);
    }
}
