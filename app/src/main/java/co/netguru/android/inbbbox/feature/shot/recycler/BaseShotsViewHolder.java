package co.netguru.android.inbbbox.feature.shot.recycler;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

import static co.netguru.android.inbbbox.common.utils.AnimationUtil.ALPHA_MAX;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.ALPHA_MIN;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.animateAlpha;
import static co.netguru.android.inbbbox.common.utils.UnitsUtils.dpToPx;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.scaleView;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.translateView;

public abstract class BaseShotsViewHolder<T> extends BaseViewHolder<T>
        implements ItemSwipeListener {

    private static final int COMMENT_ICON_PADDING_DP = 24;
    private static final float HALF = 0.5f;
    private static final float COMMENT_FILLED_ICON_ALPHA_START_PROGRESS = 0.25f;
    private static final float COMMENT_ICON_FADE_START_PROGRESS = 0.8f;
    private static final float COMMENT_ICON_FADE_END_PROGRESS = 0.9f;
    private static final float FOLLOW_ICON_ALPHA_ANIMATION_START_PROGRESS = 0.85f;
    private static final float FOLLOW_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS = 0.92f;
    private static final float FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS = 1;
    private static final float LIKE_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS = 0.25f;
    private static final float LIKE_ICON_FILLED_ALPHA_ANIMATION_END_PROGRESS = 0.45f;
    private static final float BUCKET_ICON_ALPHA_ANIMATION_START_PROGRESS = 0.8f;
    private static final float BUCKET_ICON_ALPHA_ANIMATION_END_PROGRESS = 0.95f;
    @BindView(R.id.iv_shot_image)
    protected RoundedCornersShotImageView shotImageView;
    @BindView(R.id.long_swipe_layout)
    @Nullable
    protected LongSwipeLayout longSwipeLayout;
    @Nullable
    @BindView(R.id.iv_like_action)
    ImageView likeIcon;
    @Nullable
    @BindView(R.id.iv_like_action_full)
    ImageView likeIconFull;
    @Nullable
    @BindView(R.id.iv_plus_image)
    ImageView plusIcon;
    @Nullable
    @BindView(R.id.iv_bucket_action)
    ImageView bucketIcon;
    @Nullable
    @BindView(R.id.iv_bucket_action_full)
    ImageView bucketIconFull;
    @Nullable
    @BindView(R.id.iv_comment)
    ImageView commentIcon;
    @Nullable
    @BindView(R.id.iv_comment_full)
    ImageView commentIconFull;
    @Nullable
    @BindView(R.id.iv_follow)
    ImageView followIcon;
    @Nullable
    @BindView(R.id.iv_follow_full)
    ImageView followIconFull;
    @Nullable
    @BindView(R.id._left_wrapper)
    ViewGroup leftWrapper;

    protected BaseShotsViewHolder(View view) {
        super(view);

        if (longSwipeLayout != null) {
            longSwipeLayout.setItemSwipeListener(this);
        }
    }

    @Override
    public void onSwipeProgress(int swipePosition, int swipeLimit) {
        float progress = (float) swipePosition / (float) swipeLimit;

        if (swipePosition > 0) {
            handleLeftSwipe(progress, swipePosition);
        } else {
            handleRightSwipe(-progress, swipeLimit);
        }
    }


    private void handleLeftSwipe(float progress, int swipePosition) {
        handleLikeAnimation(swipePosition, progress);
        handlePlusAndBucketAnimation(swipePosition);
    }

    protected void handleRightSwipe(float progress, int swipeLimit) {
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

    protected void handlePlusAndBucketAnimation(int swipePosition) {
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
        if (followIcon != null) {
            animateAlpha(followIcon, FOLLOW_ICON_ALPHA_ANIMATION_START_PROGRESS,
                    FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS, progress);
            animateAlpha(followIconFull, FOLLOW_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS,
                    FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS, progress);
        }
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
}
