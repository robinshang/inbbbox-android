package co.netguru.android.inbbbox.controler;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.api.ShotsApi;
import co.netguru.android.inbbbox.controler.buckets.BucketsController;
import co.netguru.android.inbbbox.controler.likes.LikeShotController;
import co.netguru.android.inbbbox.model.api.UserEntity;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import co.netguru.android.inbbbox.model.ui.User;
import rx.Completable;
import rx.Observable;
import rx.Single;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotDetailsController {

    private static final int COMMENTS_PER_PAGE = 10;
    private final LikeShotController likeShotController;
    private final BucketsController bucketsController;
    private final UserController userController;
    private final ShotsApi shotsApi;

    @Inject
    public ShotDetailsController(LikeShotController likeShotController, BucketsController bucketsController,
                                 ShotsApi shotsApi, UserController userController) {
        this.likeShotController = likeShotController;
        this.bucketsController = bucketsController;
        this.shotsApi = shotsApi;
        this.userController = userController;
    }

    public Observable<ShotDetailsState> getShotComments(Shot shot, int pageNumber) {
        return Observable.zip(getCommentListWithAuthorState(Long.toString(shot.id()), pageNumber),
                getLikeState(shot),
                getBucketState(shot.id()),
                (comments, isLiked, isBucketed) -> ShotDetailsState
                        .create(isLiked, isBucketed, comments));
    }

    public Completable performLikeAction(Shot shot, boolean newLikeState) {
        return newLikeState ? likeShotController.likeShot(shot) :
                likeShotController.unLikeShot(shot);
    }

    public Single<Comment> sendComment(Long shotId, String commentText) {
        return getCurrentUserId()
                .flatMap(currentUserId -> createComment(shotId.toString(),
                        commentText,
                        currentUserId));
    }

    public Single<Comment> updateComment(Long shotId, Long commentText, String comment) {
        return getCurrentUserId()
                .flatMap(currentUserId -> sendUpdateCommentRequest(shotId.toString(),
                        commentText.toString(),
                        currentUserId,
                        comment)
                );
    }

    private Single<Comment> sendUpdateCommentRequest(String shotId,
                                                     String commentText,
                                                     Long currentUserId,
                                                     String comment) {
        return shotsApi.updateComment(shotId, commentText, comment)
                .map(commentEntity ->
                        Comment.create(commentEntity,
                                isCurrentUserAuthor(commentEntity.getUser(), currentUserId)));
    }

    public Completable deleteComment(Long shotId, Long commentId) {
        return shotsApi.deleteComment(shotId.toString(), commentId.toString());
    }

    private Single<Comment> createComment(String shotId, String commentText, Long currentUserId) {
        return shotsApi.createComment(shotId, commentText)
                .map(commentEntity ->
                        Comment.create(commentEntity,
                                isCurrentUserAuthor(commentEntity.getUser(), currentUserId)));
    }

    private Observable<Boolean> getBucketState(long shotId) {
        return getCurrentUserId()
                .flatMap(userId -> bucketsController.isShotBucketed(shotId, userId))
                .toObservable();
    }

    private Single<Long> getCurrentUserId() {
        return userController.getUserFromCache()
                .map(User::id)
                .onErrorResumeNext(throwable -> Single.just((long) Constants.UNDEFINED));
    }

    private Observable<List<Comment>> getCommentListWithAuthorState(String shotId, int pageNumber) {
        return getCurrentUserId()
                .flatMapObservable(currentUserId -> getCommentsList(shotId, currentUserId, pageNumber));
    }

    private Observable<List<Comment>> getCommentsList(String shotId, Long currentUserId, int pageNumber) {
        return shotsApi.getShotComments(shotId, pageNumber, COMMENTS_PER_PAGE)
                .compose(fromListObservable())
                .map(commentEntity -> Comment.create(commentEntity,
                        isCurrentUserAuthor(commentEntity.getUser(), currentUserId)))
                .toList()
                .map(this::reverseList)
                .onErrorResumeNext(throwable -> Observable.just(Collections.emptyList()));
    }

    private List<Comment> reverseList(List<Comment> comments) {
        Collections.reverse(comments);
        return comments;
    }

    private boolean isCurrentUserAuthor(UserEntity user, Long currentUserId) {
        return user != null && user.id() == currentUserId;
    }

    private Observable<Boolean> getLikeState(Shot shot) {
        return likeShotController.isShotLiked(shot)
                .andThen(Observable.just(Boolean.TRUE))
                .onErrorResumeNext(Observable.just(Boolean.FALSE));
    }
}
