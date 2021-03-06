package co.netguru.android.inbbbox.feature.shot.detail;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.common.utils.StringUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.UserShotsController;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.ShotUpdatedEvent;
import rx.Completable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applyCompletableIoSchedulers;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

@FragmentScope
public class ShotDetailsPresenter
        extends MvpNullObjectBasePresenter<ShotDetailsContract.View>
        implements ShotDetailsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;

    private final ShotDetailsController shotDetailsController;
    private final ErrorController errorController;
    private final RxBus rxBus;
    private final BucketsController bucketsController;
    private final UserShotsController userShotsController;
    private final CompositeSubscription subscriptions;
    private boolean isCommentModeInit;
    private Shot shot;
    private List<Shot> allShots;
    private Comment commentInEditor;
    private CommentLoadMoreState commentLoadMoreState;
    private int pageNumber = 1;
    private int commentsCounter = 0;
    private boolean isInOneBucket;
    private boolean isInMoreBuckets;
    private int currentIndex;
    @Nullable
    private List<Bucket> bucketListShotBelongsTo;

    @Inject
    ShotDetailsPresenter(ShotDetailsController shotDetailsController,
                         ErrorController errorController,
                         List<Shot> allShots,
                         RxBus rxBus,
                         BucketsController bucketsController,
                         UserShotsController userShotsController) {
        this.shotDetailsController = shotDetailsController;
        this.errorController = errorController;
        this.bucketsController = bucketsController;
        this.userShotsController = userShotsController;
        this.rxBus = rxBus;
        this.subscriptions = new CompositeSubscription();
        this.commentLoadMoreState = new CommentLoadMoreState();
        this.allShots = allShots;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void downloadData() {
        enableInputWhenIfInCommentMode();
        initializeView();

        downloadCommentsFromAPI();
    }

    @Override
    public void handleShotLike(boolean newLikeState) {
        subscriptions.add(
                shotDetailsController
                        .performLikeAction(shot, newLikeState)
                        .compose(applyCompletableIoSchedulers())
                        .subscribe(() -> updateLikeState(newLikeState),
                                throwable -> handleError(throwable,
                                        "Error while performing like action"))
        );
    }

    @Override
    public void retrieveInitialData() {
        this.shot = getView().getShotInitialData();
        this.currentIndex = allShots.indexOf(shot);
        this.isCommentModeInit = getView().getCommentModeInitialState();
    }

    @Override
    public void sendComment() {
        String comment = getView().getCommentText();
        if (!StringUtil.isBlank(comment)) {
            getView().showSendingCommentIndicator();
            sendCommentToApi(comment);
        }
    }

    @Override
    public void onEditCommentClick(Comment currentComment) {
        commentInEditor = currentComment;
        getView().showCommentEditorDialog(currentComment.text());
    }

    @Override
    public void updateComment(String updatedComment) {
        subscriptions.add(
                shotDetailsController.updateComment(shot.id(),
                        commentInEditor.id(),
                        updatedComment)
                        .compose(applySingleIoSchedulers())
                        .subscribe(this::handleCommentUpdated,
                                throwable -> handleError(throwable, "Error while updating comment"))
        );
    }

    @Override
    public void closeScreen() {
        getView().hideDetailsScreen();
    }

    @Override
    public void onCommentDelete(Comment currentComment) {
        commentInEditor = currentComment;
        getView().showDeleteCommentWarning();
    }

    @Override
    public void onCommentDeleteConfirmed() {
        subscriptions.add(
                shotDetailsController
                        .deleteComment(shot.id(), commentInEditor.id())
                        .compose(applyCompletableIoSchedulers())
                        .subscribe(this::handleCommentDeleteComplete,
                                throwable -> handleError(throwable,
                                        "Error while deleting comment"))
        );
    }

    @Override
    public void getMoreComments() {
        pageNumber++;
        commentLoadMoreState.setLoadMoreActive(false);
        commentLoadMoreState.setWaitingForUpdate(true);
        getView().updateLoadMoreState(commentLoadMoreState);

        downloadCommentsFromAPI();
    }

    @Override
    public void onShotImageClick() {
        getView().openShotFullscreen(allShots, currentIndex);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
        getView().disableEditorProgressMode();
    }

    @Override
    public void addShotToBucket(Bucket bucket, Shot shot) {
        subscriptions.add(
                bucketsController.addShotToBucket(bucket.id(), shot)
                        .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                        .subscribe(() -> updateShotAndShowAddToBucketSuccess(shot),
                                throwable -> handleError(throwable,
                                        "Error while adding shot to bucket"))
        );
    }

    @Override
    public void removeShotFromBuckets(List<Bucket> list, Shot shot) {
        List<Completable> bucketsToRemove = new ArrayList<>();
        for (Bucket bucket : list) {
            bucketsToRemove.add(bucketsController.removeShotFromBucket(bucket.id(), shot));
        }
        subscriptions.add(
                Completable.merge(bucketsToRemove)
                        .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                        .subscribe(() -> handleShotRemovedFromBucket(shot),
                                throwable -> handleError(throwable,
                                        "Error while removing shot from bucket"))
        );
    }

    @Override
    public void checkShotBucketsCount(Shot shot) {
        subscriptions.add(bucketsController.getListBucketsForShot(shot.id())
                .compose(RxTransformerUtil.applySingleIoSchedulers())
                .subscribe(this::verifyShotBucketsCount,
                        throwable -> handleError(throwable,
                                "Error occurred while requesting buckets")));
    }

    @Override
    public void onShotBucketClicked(Shot shot) {
        if (shot != null) {
            verifyShotBucketState(shot);
        }
    }

    @Override
    public void getTeamUserWithShots(Team team) {
        subscriptions.add(userShotsController.getTeamUserWithShots(team, pageNumber,
                SHOT_PAGE_COUNT, true)
                .compose(androidIO())
                .subscribe(this::showTeamView,
                        throwable -> handleError(throwable, "Error while getting user wit shots")));
    }

    private void showTeamView(UserWithShots userWithShots) {
        getView().showTeamView(userWithShots);
    }

    private void verifyShotBucketState(Shot shot) {
        if (isInMoreBuckets) {
            getView().showRemoveShotFromBucketView(shot);
        } else if (isInOneBucket) {
            removeShotFromBuckets(bucketListShotBelongsTo, shot);
        } else {
            getView().showAddShotToBucketView(shot);
        }
    }

    private void initializeView() {
        getView().initView();
        showShotMainImage();
        getView().updateLoadMoreState(commentLoadMoreState);
        getView().setInputShowingEnabled(false);
        showShotDetails(shot);
    }

    private void showShotMainImage() {
        if (shot.isGif()) {
            getView().showMainImageWithGifAnimation(shot);
        } else {
            getView().showMainImage(shot);
        }
    }

    private void enableInputWhenIfInCommentMode() {
        if (isCommentModeInit) {
            getView().showInputIfHidden();
            getView().showKeyboard();
            getView().requestFocusOnCommentInput();
        }
    }

    private void handleCommentDeleteComplete() {
        shot = Shot.update(shot).commentsCount(shot.commentsCount() - 1).build();
        getView().removeCommentFromView(commentInEditor);
        getView().showInfo(R.string.comment_deleted_complete);
        rxBus.send(new ShotUpdatedEvent(shot));
    }

    private void sendCommentToApi(String comment) {
        subscriptions.add(
                shotDetailsController.sendComment(shot.id(), comment)
                        .compose(applySingleIoSchedulers())
                        .doAfterTerminate(this::handleSaveCommentTermination)
                        .subscribe(this::handleCommentSavingComplete,
                                throwable -> handleError(throwable, "Error while sending comment"))
        );
    }

    private void handleSaveCommentTermination() {
        getView().hideSendingCommentIndicator();
        getView().hideKeyboard();
    }

    private void handleCommentSavingComplete(Comment updatedComment) {
        getView().hideSendingCommentIndicator();
        getView().addNewComment(updatedComment);
        getView().clearCommentInput();
        shot = Shot.update(shot).commentsCount(shot.commentsCount() + 1).build();
        rxBus.send(new ShotUpdatedEvent(shot));
    }

    private void updateLikeState(boolean newLikeState) {
        shot = Shot.update(shot)
                .isLiked(newLikeState)
                .likesCount(newLikeState ? shot.likesCount() + 1 : shot.likesCount() - 1)
                .build();
        showShotDetails(shot);
        rxBus.send(new ShotUpdatedEvent(shot));
    }

    private void handleDetailsStates(ShotDetailsState state) {
        List<Comment> comments = state.comments();
        commentsCounter += comments.size();

        getView().addCommentsToList(comments);
        updateLoadMoreState();

        updateShotDetails(state.isLiked(), state.isBucketed());

        checkCommentMode();
    }

    private void updateLoadMoreState() {
        commentLoadMoreState.setWaitingForUpdate(false);
        commentLoadMoreState.setLoadMoreActive(commentsCounter < shot.commentsCount());
        getView().updateLoadMoreState(commentLoadMoreState);
    }

    private void checkCommentMode() {
        if (isCommentModeInit) {
            getView().collapseAppbarWithAnimation();
            getView().scrollToLastItem();
        }
    }

    private void downloadCommentsFromAPI() {
        subscriptions.add(
                shotDetailsController.getShotComments(shot, pageNumber)
                        .compose(androidIO())
                        .doOnCompleted(() -> getView().setInputShowingEnabled(true))
                        .subscribe(this::handleDetailsStates,
                                throwable -> handleError(throwable,
                                        "Error while getting shot comments"))
        );
    }

    private void updateShotDetails(boolean liked, boolean bucketed) {
        if (shot.isLiked() != liked || shot.isBucketed() != bucketed) {
            shot = Shot.update(shot)
                    .isLiked(liked)
                    .isBucketed(bucketed)
                    .build();
            showShotDetails(shot);
        }
    }

    private void showShotDetails(Shot shotDetails) {
        Timber.d("Shot details received: %s", shotDetails);
        getView().showDetails(shotDetails);
    }

    private void handleCommentUpdated(Comment comment) {
        getView().dismissCommentEditor();
        getView().updateComment(commentInEditor, comment);
        getView().showInfo(R.string.comment_update_complete);
    }

    private void verifyShotBucketsCount(List<Bucket> bucketList) {
        bucketListShotBelongsTo = bucketList;
        if (bucketList.size() == 1) {
            setShotBucketStates(true, false);
            updateBucketedDataAndShowDetails(true, bucketList.size());
        } else if (bucketList.size() > 1) {
            setShotBucketStates(false, true);
            updateBucketedDataAndShowDetails(true, bucketList.size());
        } else {
            setShotBucketStates(false, false);
            updateBucketedDataAndShowDetails(false, bucketList.size());
        }
    }

    private void setShotBucketStates(boolean isInOneBucket, boolean isInMoreBuckets) {
        this.isInOneBucket = isInOneBucket;
        this.isInMoreBuckets = isInMoreBuckets;
    }

    private void updateBucketedDataAndShowDetails(boolean isBucketed, int bucketCount) {
        shot = Shot.update(shot).isBucketed(isBucketed).bucketCount(bucketCount).build();
        getView().showDetails(shot);
    }

    private void handleShotRemovedFromBucket(Shot shot) {
        getView().showShotRemoveFromBucketSuccess();
        checkShotBucketsCount(shot);
        rxBus.send(new ShotUpdatedEvent(shot));
    }

    private void updateShotAndShowAddToBucketSuccess(Shot shot) {
        getView().showBucketAddSuccess();
        checkShotBucketsCount(shot);
        rxBus.send(new ShotUpdatedEvent(shot));
    }
}
