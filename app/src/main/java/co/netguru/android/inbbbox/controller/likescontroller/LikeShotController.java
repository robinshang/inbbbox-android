package co.netguru.android.inbbbox.controller.likescontroller;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;

public interface LikeShotController {

    Completable isShotLiked(Shot shot);

    Completable likeShot(Shot shot);

    Completable unLikeShot(Shot shot);

    Observable<List<Shot>> getLikedShots(int pageNumber, int pageCount);
}
