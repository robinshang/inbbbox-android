package co.netguru.android.inbbbox.controler.likescontroller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.controler.LikeShotController;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;

@Singleton
public class LikeShotControllerApi implements LikeShotController {

    private final LikesApi likesApi;

    @Inject
    public LikeShotControllerApi(LikesApi likesApi) {
        this.likesApi = likesApi;
    }

    @Override
    public Completable isShotLiked(Shot shot) {
        return likesApi.isShotLiked(shot.id());
//                .compose(guestModeController.getIsShotLikedTransformer(shot));
    }

    @Override
    public Completable likeShot(Shot shot) {
        return likesApi.likeShot(shot.id());
//                .compose(guestModeController.getShotLikeTransformer(shot));
    }

    @Override
    public Completable unLikeShot(Shot shot) {
        return likesApi.unLikeShot(shot.id());
//                .compose(guestModeController.getShotUnlikeTransformer(shot));
    }

    @Override
    public Observable<List<Shot>> getLikedShots(int pageNumber, int pageCount) {
        return likesApi.getLikedShots(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(likedShotEntity -> Shot.create(likedShotEntity.shot()))
                .toList();
    }
}