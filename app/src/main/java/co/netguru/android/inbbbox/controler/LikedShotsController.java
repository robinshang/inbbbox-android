package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.model.ui.LikedShot;
import rx.Observable;

@Singleton
public class LikedShotsController {

    private final LikesApi likesApi;

    @Inject
    LikedShotsController(LikesApi likesApi) {
        this.likesApi = likesApi;
    }

    public Observable<LikedShot> getLikedShots() {
        return likesApi.getLikedShots()
                .flatMap(Observable::from)
                .map(LikedShot::new);
    }
}
