package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;

@Singleton
public class LikeShotControllerApi {

    private final GuestModeController guestModeController;
    private final LikesApi likesApi;

    @Inject
    LikeShotControllerApi(GuestModeController guestModeController, LikesApi likesApi) {
        this.guestModeController = guestModeController;
        this.likesApi = likesApi;
    }

    public Completable isShotLiked(Shot shot) {
        return likesApi.isShotLiked(shot.id())
                .compose(guestModeController.getIsShotLikedTransformer(shot));
    }

    public Completable likeShot(Shot shot) {
        return likesApi.likeShot(shot.id())
                .compose(guestModeController.getShotLikeTransformer(shot));
    }

    public Completable unLikeShot(Shot shot) {
        return likesApi.unLikeShot(shot.id())
                .compose(guestModeController.getShotUnlikeTransformer(shot));
    }
}