package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Observable;

@Singleton
public class LikedShotsController {

    private final LikesApi likesApi;
    private final GuestModeController guestModeController;

    @Inject
    LikedShotsController(LikesApi likesApi, GuestModeController guestModeController) {
        this.likesApi = likesApi;
        this.guestModeController = guestModeController;
    }

    public Observable<Shot> getLikedShots(int pageNumber, int pageCount) {
        return likesApi.getLikedShots(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(likedShotEntity -> Shot.create(likedShotEntity.shot()))
                .compose(guestModeController.getLikedShotTransformer());
    }
}
