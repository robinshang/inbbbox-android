package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.LikesApi;
import rx.Completable;

@Singleton
public class LikeShotController {

    private final GuestModeController guestModeController;
    private final LikesApi likesApi;

    @Inject
    LikeShotController(GuestModeController guestModeController, LikesApi likesApi) {
        this.guestModeController = guestModeController;
        this.likesApi = likesApi;
    }

    public Completable isShotLiked(long id) {
        return likesApi.isShotLiked(id)
                .compose(guestModeController.getTransformerForIsShotLiked(id));
    }

    public Completable likeShot(long id) {
        return likesApi.likeShot(id)
                .compose(guestModeController.getTransformerForShotLike(id));
    }

    public Completable unLikeShot(long id) {
        return likesApi.unLikeShot(id);
    }
}