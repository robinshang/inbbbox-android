package co.netguru.android.inbbbox.data.like.controllers;

import java.util.List;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class LikeShotControllerApi implements LikeShotController {

    private final LikesApi likesApi;

    public LikeShotControllerApi(LikesApi likesApi) {
        this.likesApi = likesApi;
    }

    @Override
    public Single<Boolean> isShotLiked(Shot shot) {
        return likesApi.isShotLiked(shot.id())
                .andThen(Single.just(Boolean.TRUE))
                .onErrorResumeNext(Single.just(Boolean.FALSE));
    }

    @Override
    public Completable likeShot(Shot shot) {
        return likesApi.likeShot(shot.id());
    }

    @Override
    public Completable unLikeShot(Shot shot) {
        return likesApi.unLikeShot(shot.id());
    }

    @Override
    public Observable<List<Shot>> getLikedShots(int pageNumber, int pageCount) {
        return likesApi.getLikedShots(pageNumber, pageCount)
                .flatMap(Observable::from)
                .map(likedShotEntity -> Shot.create(likedShotEntity.shot()))
                .toList();
    }
}