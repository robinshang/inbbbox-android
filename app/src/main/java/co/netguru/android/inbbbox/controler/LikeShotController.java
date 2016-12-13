package co.netguru.android.inbbbox.controler;

import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;

public interface LikeShotController {

    Completable isShotLiked(Shot shot);

    Completable likeShot(Shot shot);

    Completable unLikeShot(Shot shot);
}
