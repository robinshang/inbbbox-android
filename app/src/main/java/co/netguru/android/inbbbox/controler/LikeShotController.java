package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.api.LikesApi;

import rx.Completable;

@Singleton
public class LikeShotController {

    private final LikesApi likesApi;

    @Inject
    LikeShotController(LikesApi likesApi) {
        this.likesApi = likesApi;
    }

    public Completable isShotLiked(long id) {
        return likesApi.isShotLiked(id);
    }

    public Completable likeShot(long id) {
        return likesApi.likeShot(id);
    }

    public Completable unLikeShot(long id) {
        return likesApi.unLikeShot(id);
    }
}