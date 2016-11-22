package co.netguru.android.inbbbox.controler;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import rx.Completable;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotDetailsController {

    private final ShotCommentsApi shotCommentsApi;
    private final LikeShotController likeShotController;

    @Inject
    public ShotDetailsController(ShotCommentsApi shotCommentsApi,
                                 LikeShotController likeShotController) {
        this.shotCommentsApi = shotCommentsApi;
        this.likeShotController = likeShotController;
    }

    public Observable<ShotDetailsState> getShotComments(Long shotId) {
        return Observable.zip(getCommentList(shotId.toString()),
                getLikeState(shotId),
                getBucketState(shotId),
                (comments, isLiked, isBucketed) -> ShotDetailsState
                        .create(isLiked, isBucketed, comments));
    }

    private Observable<Boolean> getBucketState(Long shotId) {
        // TODO: 21.11.2016 not in scope of this task
        // - return observable with boolean state if this shout belongs to some bucket of this user
        return Observable.just(Boolean.FALSE);
    }

    private Observable<List<Comment>> getCommentList(String shotId) {
        return shotCommentsApi.getComments(shotId)
                .compose(fromListObservable())
                .map(Comment::create)
                .distinct()
                .toList()
                .onErrorResumeNext(throwable -> Observable.just(Collections.emptyList()));
    }

    private Observable<Boolean> getLikeState(Long shotId) {
        return likeShotController.isShotLiked(shotId)
                .andThen(Observable.just(Boolean.TRUE))
                .onErrorResumeNext(Observable.just(Boolean.FALSE));
    }

    public Completable performLikeAction(long shotId, boolean newLikeState) {
        return newLikeState ? likeShotController.likeShot(shotId) :
                likeShotController.unLikeShot(shotId);
    }
}
