package co.netguru.android.inbbbox.data.like.controllers;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Completable;
import rx.Observable;

public interface LikeShotController {

    Completable isShotLiked(Shot shot);

    Completable likeShot(Shot shot);

    Completable unLikeShot(Shot shot);

    Observable<List<Shot>> getLikedShots(int pageNumber, int pageCount);
}
