package co.netguru.android.inbbbox.controler;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.model.api.UserEntity;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.inbbbox.model.ui.User;
import rx.Completable;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotDetailsController {

    private static final long REQUEST_COUNT = 2;
    private final LikeShotController likeShotController;
    private final UserController userController;
    private final ShotsApi shotsApi;

    @Inject
    public ShotDetailsController(LikeShotController likeShotController,
                                 ShotsApi shotsApi,
                                 UserController userController) {
        this.shotsApi = shotsApi;
        this.likeShotController = likeShotController;
        this.userController = userController;
    }

    public Observable<ShotDetailsState> getShotComments(Long shotId) {
        return Observable.zip(getCommentListWithAuthorState(shotId.toString()),
                getLikeState(shotId),
                getBucketState(shotId),
                (comments, isLiked, isBucketed) -> ShotDetailsState
                        .create(isLiked, isBucketed, comments));
    }

    private Observable<Boolean> getBucketState(long shotId) {
        return Observable.zip(getCurrentBuckets(shotId),
                getCurrentUserId(),
                List::contains)
                .onErrorResumeNext(Observable.just(Boolean.FALSE));
    }

    private Observable<Long> getCurrentUserId() {
        return userController.getUserFromCache()
                .toObservable()
                .map(User::id)
                .repeat(REQUEST_COUNT);
    }

    private Observable<List<Long>> getCurrentBuckets(Long shotId) {
        return shotsApi.getBucketsList(shotId.toString())
                .compose(fromListObservable())
                .map(bucket -> bucket.user() != null ? bucket.user().id() : (long) Constants.UNDEFINED)
                .toList();
    }

    private Observable<List<Comment>> getCommentListWithAuthorState(String shotId) {
        return getCurrentUserId().flatMap(currentUserId -> getCommentsList(shotId, currentUserId));

    }

    private Observable<List<Comment>> getCommentsList(String shotId, Long currentUserId) {
        return shotsApi.getShotComments(shotId)
                .compose(fromListObservable())
                .map(commentEntity -> Comment.create(commentEntity,
                        isCurrentUserAuthor(commentEntity.getUser(), currentUserId)))
                .distinct()
                .toList()
                .onErrorResumeNext(throwable -> Observable.just(Collections.emptyList()));
    }

    private boolean isCurrentUserAuthor(UserEntity user, Long currentUserId) {
        return user != null && user.id() == currentUserId;
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
