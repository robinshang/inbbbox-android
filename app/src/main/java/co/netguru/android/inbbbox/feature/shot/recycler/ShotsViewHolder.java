package co.netguru.android.inbbbox.feature.shot.recycler;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

import static co.netguru.android.inbbbox.common.utils.AnimationUtil.ALPHA_MAX;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.ALPHA_MIN;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.animateAlpha;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.dpToPx;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.scaleView;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.translateView;

class ShotsViewHolder extends BaseViewHolder<Shot>
        implements ItemSwipeListener {

    public static final int COMMENT_ICON_PADDING_DP = 24;
    public static final float HALF = 0.5f;
    public static final float COMMENT_FILLED_ICON_ALPHA_START_PROGRESS = 0.25f;
    public static final float COMMENT_ICON_FADE_START_PROGRESS = 0.8f;
    public static final float COMMENT_ICON_FADE_END_PROGRESS = 0.9f;
    private static final float FOLLOW_ICON_ALPHA_ANIMATION_START_PROGRESS = 0.85f;
    private static final float FOLLOW_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS = 0.92f;
    private static final float FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS = 1;
    private static final float LIKE_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS = 0.25f;
    private static final float LIKE_ICON_FILLED_ALPHA_ANIMATION_END_PROGRESS = 0.45f;
    private static final float BUCKET_ICON_ALPHA_ANIMATION_START_PROGRESS = 0.8f;
    private static final float BUCKET_ICON_ALPHA_ANIMATION_END_PROGRESS = 0.95f;
    private final ShotSwipeListener shotSwipeListener;
    @BindView(R.id.long_swipe_layout)
    LongSwipeLayout longSwipeLayout;
    @BindView(R.id.iv_shot_image)
    RoundedCornersShotImageView shotImageView;
    @BindView(R.id.iv_like_action)
    ImageView likeIcon;
    @BindView(R.id.iv_like_action_full)
    ImageView likeIconFull;
    @BindView(R.id.iv_plus_image)
    ImageView plusIcon;
    @BindView(R.id.iv_bucket_action)
    ImageView bucketIcon;
    @BindView(R.id.iv_bucket_action_full)
    ImageView bucketIconFull;
    @BindView(R.id.iv_comment)
    ImageView commentIcon;
    @BindView(R.id.iv_comment_full)
    ImageView commentIconFull;
    @BindView(R.id.iv_follow)
    ImageView followIcon;
    @BindView(R.id.iv_follow_full)
    ImageView followIconFull;
    @BindView(R.id._left_wrapper)
    RelativeLayout leftWrapper;
    @BindView(R.id.user_name_textView)
    TextView userNameTextView;
    @BindView(R.id.comments_count_textView)
    TextView commentsCountTextView;
    @BindView(R.id.likes_count_textView)
    TextView likesCountTextView;
    @BindView(R.id.user_imageView)
    ImageView userImageView;
    @BindDrawable(R.drawable.shot_placeholder)
    Drawable shotPlaceHolder;
    private Shot shot;

    ShotsViewHolder(View itemView, @NonNull ShotSwipeListener shotSwipeListener) {
        super(itemView);
        this.shotSwipeListener = shotSwipeListener;
        longSwipeLayout.setItemSwipeListener(this);
    }

    @OnClick(R.id.iv_shot_image)
    void onShotClick() {
        shotSwipeListener.onShotSelected(shot);
    }

    @Override
    public void bind(Shot shot) {
        this.shot = shot;
        setupImage(shot);
        setupDetails(shot);
        loadUserImage(shot.author().avatarUrl());
    }

    @Override
    public void onLeftSwipe() {
        shotSwipeListener.onShotLikeSwipe(shot);
    }

    @Override
    public void onLeftLongSwipe() {
        shotSwipeListener.onAddShotToBucketSwipe(shot);
    }

    @Override
    public void onRightSwipe() {
        shotSwipeListener.onCommentShotSwipe(shot);
    }

    @Override
    public void onRightLongSwipe() {
        shotSwipeListener.onFollowUserSwipe(shot);
    }

    @Override
    public void onSwipeProgress(int swipePosition, int swipeLimit) {
        float progress = (float) swipePosition / (float) swipeLimit;

        if (swipePosition > 0) {
            handleLeftSwipe(progress, swipePosition, swipeLimit);
        } else {
            handleRightSwipe(-progress, swipePosition, swipeLimit);
        }
    }

    @Override
    public void onStartSwipe() {
        shotSwipeListener.onStartSwipe(shot);
    }

    @Override
    public void onEndSwipe() {
        shotSwipeListener.onEndSwipe(shot);
    }

    private void handleLeftSwipe(float progress, int swipePosition, int swipeLimit) {
        handleLikeAnimation(swipePosition, progress);
        handlePlusAndBucketAnimation(swipePosition);
    }

    private void handleRightSwipe(float progress, int swipePosition, int swipeLimit) {
        handleFollowIconSwipe(progress);
        handleCommentIconSwipe(progress, swipeLimit);
    }

    private void handleLikeAnimation(int swipePosition, float progress) {
        int likeIconLeft = likeIcon.getLeft();
        float likePercent = getPercent(swipePosition, likeIconLeft, likeIcon.getWidth());

        translateView(likeIcon, swipePosition, likePercent);
        translateView(likeIconFull, swipePosition, likePercent);
        scaleView(likeIcon, likePercent);
        scaleView(likeIconFull, likePercent);

        animateAlpha(likeIconFull, LIKE_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS,
                LIKE_ICON_FILLED_ALPHA_ANIMATION_END_PROGRESS, progress);
    }

    private void handlePlusAndBucketAnimation(int swipePosition) {
        float progressFactor = (float) swipePosition / (float) leftWrapper.getWidth();
        float plusPercent = getPercent(swipePosition, plusIcon.getLeft(), plusIcon.getWidth());
        float bucketPercent = getPercent(swipePosition, bucketIcon.getLeft(), bucketIcon.getWidth());

        scaleView(plusIcon, plusPercent);
        scaleView(bucketIcon, bucketPercent);
        scaleView(bucketIconFull, bucketPercent);

        animateAlpha(bucketIconFull, BUCKET_ICON_ALPHA_ANIMATION_START_PROGRESS,
                BUCKET_ICON_ALPHA_ANIMATION_END_PROGRESS, progressFactor);
        animateAlpha(bucketIcon, BUCKET_ICON_ALPHA_ANIMATION_START_PROGRESS,
                BUCKET_ICON_ALPHA_ANIMATION_END_PROGRESS, progressFactor, true);
    }

    private void handleFollowIconSwipe(float progress) {
        animateAlpha(followIcon, FOLLOW_ICON_ALPHA_ANIMATION_START_PROGRESS,
                FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS, progress);
        animateAlpha(followIconFull, FOLLOW_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS,
                FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS, progress);
    }

    private void handleCommentIconSwipe(float progress, int swipeLimit) {
        float margin = dpToPx(COMMENT_ICON_PADDING_DP);

        // this is the progress at which comment icon is in the center horizontally
        float commentCenterProgress = HALF +
                ((commentIcon.getWidth() + margin) * HALF) / swipeLimit;

        handleCommentIconTranslationAndScale(progress, margin, commentCenterProgress, swipeLimit);
        handleCommentIconAlpha(progress, commentCenterProgress);
    }

    private void handleCommentIconTranslationAndScale(float progress, float margin,
                                                      float commentCenterProgress, float swipeLimit) {
        if (progress < commentCenterProgress) {
            // translation
            float translationX = (-progress * swipeLimit) + commentIcon.getWidth() + margin;
            commentIcon.setTranslationX(translationX);
            commentIconFull.setTranslationX(translationX);

            // scale
            float scale = progress / commentCenterProgress;
            scaleView(commentIcon, scale, 0, commentIcon.getHeight() * HALF);
            scaleView(commentIconFull, scale, 0, commentIconFull.getHeight() * HALF);
        }
    }

    private void handleCommentIconAlpha(float progress, float commentCenterProgress) {
        float alphaProgressStart = commentCenterProgress - COMMENT_FILLED_ICON_ALPHA_START_PROGRESS;
        float alphaProgressEnd = commentCenterProgress;

        if (progress >= alphaProgressStart && progress <= alphaProgressEnd) {
            float alpha = (progress - alphaProgressStart) / (alphaProgressEnd - alphaProgressStart);

            commentIconFull.setAlpha(alpha);
            commentIcon.setAlpha(ALPHA_MAX - alpha);
        } else if (progress < alphaProgressStart) {
            commentIcon.setAlpha(ALPHA_MAX);
            commentIconFull.setAlpha(ALPHA_MIN);
        } else if (progress > COMMENT_ICON_FADE_START_PROGRESS) {
            commentIcon.setAlpha(ALPHA_MIN);

            if (progress < COMMENT_ICON_FADE_END_PROGRESS) {
                float alpha = ALPHA_MAX - ((progress - COMMENT_ICON_FADE_START_PROGRESS) /
                        (COMMENT_ICON_FADE_END_PROGRESS - COMMENT_ICON_FADE_START_PROGRESS));
                commentIconFull.setAlpha(alpha);
            } else {
                commentIconFull.setAlpha(ALPHA_MIN);
            }
        }
    }

    private float getPercent(int progress, int viewLeft, int viewWidth) {
        if (viewLeft != 0 || viewWidth != 0) {
            return (float) progress / (viewLeft + viewWidth);
        } else {
            return 0;
        }
    }

    private void setupImage(Shot shot) {
        shotImageView.loadShot(shot);
        likeIcon.setActivated(shot.isLiked());
    }

    private void setupDetails(Shot shot) {
        if (shot.author() != null && shot.author().username() != null)
            userNameTextView.setText(shot.author().username());

        likesCountTextView.setText(String.valueOf(shot.likesCount()));
        commentsCountTextView.setText(String.valueOf(shot.commentsCount()));
    }

    private void loadUserImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImageView);
    }
}