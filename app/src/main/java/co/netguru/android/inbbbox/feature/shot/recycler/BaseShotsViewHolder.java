package co.netguru.android.inbbbox.feature.shot.recycler;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

import static co.netguru.android.inbbbox.common.utils.AnimationUtil.ALPHA_MAX;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.ALPHA_MIN;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.animateAlpha;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.scaleView;
import static co.netguru.android.inbbbox.common.utils.AnimationUtil.translateView;
import static co.netguru.android.inbbbox.common.utils.UnitsUtils.dpToPx;

public abstract class BaseShotsViewHolder<T> extends BaseViewHolder<T>
        implements ItemSwipeListener {

    private static final int COMMENT_ICON_PADDING_DP = 24;
    private static final int LOCATION_ON_SCREEN_COORDINATES_NUMBER = 2;
    private static final int CLOSE_SHOT_DELAY_MS = 200;
    private static final int COMMENT_CLOSE_SHOT_DELAY_MS = 300;
    private static final int FINISH_ACTION_DELAY_MS = 1200;
    private static final int SHOT_MOVE_ANIMATION_DURATION = 1000;
    private static final int LIKE_FADE_ANIMATION_DURATION = 50;
    private static final int TEXT_CENTER_ANIMATION_DURATION = 900;
    private static final int ICON_CENTER_ANIMATION_DURATION = 1000;
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
    @Nullable
    @BindView(R.id.bottom_right_wrapper)
    ViewGroup bottomRightWrapper;
    //additional TextViews needed due to closing SwipeLayout when text changes
    @Nullable
    @BindView(R.id.like_done_text)
    TextView likeDoneText;
    @Nullable
    @BindView(R.id.add_to_bucket_done_text)
    TextView bucketDoneText;
    @Nullable
    @BindView(R.id.follow_done_text)
    TextView followDoneText;
    @Nullable
    @BindView(R.id.comment_done_text)
    TextView commentDoneText;

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

    protected void handleRightSwipe(float progress, int swipeLimit) {
        handleFollowIconSwipe(progress);
        handleCommentIconSwipe(progress, swipeLimit);
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

    protected void animateAndFinishLike(FinishSwipeActionListener finishSwipeActionListener) {
        fadeLikeAnimation();
        animateActionViews(new View[]{likeIcon, bucketIconFull},
                new View[]{likeIconFull},
                likeIconFull,
                likeDoneText,
                true);

        finishAction(new View[]{likeIconFull, likeDoneText, plusIcon, bucketIcon},
                new View[]{bucketIconFull, likeIcon},
                CLOSE_SHOT_DELAY_MS,
                FINISH_ACTION_DELAY_MS,
                finishSwipeActionListener);
    }

    protected void animateAndFinishAddToBucket(FinishSwipeActionListener finishSwipeActionListener) {
        animateActionViews(new View[]{bucketIcon, likeIcon},
                new View[]{plusIcon, bucketIconFull, likeIconFull},
                plusIcon,
                bucketDoneText,
                true);

        finishAction(new View[]{likeIconFull, bucketDoneText, plusIcon, bucketIconFull},
                new View[]{bucketIcon, likeIcon},
                CLOSE_SHOT_DELAY_MS,
                FINISH_ACTION_DELAY_MS,
                finishSwipeActionListener);
    }

    protected void animateAndFinishFollowUser(FinishSwipeActionListener finishSwipeActionListener) {
        animateActionViews(new View[]{followIcon},
                new View[]{followIconFull},
                followIconFull,
                followDoneText,
                false);

        finishAction(new View[]{followDoneText, followIconFull},
                new View[]{followIcon},
                CLOSE_SHOT_DELAY_MS,
                FINISH_ACTION_DELAY_MS,
                finishSwipeActionListener);
    }

    protected void animateAndFinishComment(FinishSwipeActionListener finishSwipeActionListener) {
        commentIconFull.setAlpha(ALPHA_MAX);
        animateActionViews(new View[]{commentIcon, followIcon, commentDoneText},
                new View[]{commentIconFull},
                commentIconFull,
                commentDoneText,
                false);

        finishAction(new View[]{commentDoneText, commentIconFull},
                new View[]{followIcon, commentIcon},
                COMMENT_CLOSE_SHOT_DELAY_MS,
                FINISH_ACTION_DELAY_MS,
                finishSwipeActionListener);
    }

    protected void animateShotView(boolean rightToLeft) {
        int finishLocation = rightToLeft ? -shotImageView.getWidth() : shotImageView.getWidth();
        TranslateAnimation translateAnimation = new TranslateAnimation(0, finishLocation, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(SHOT_MOVE_ANIMATION_DURATION);
        shotImageView.startAnimation(translateAnimation);
    }

    private void animateActionViews(View[] toInvisible,
                                    View[] iconsToCenter,
                                    View centerIcon,
                                    TextView textToCenter,
                                    boolean rightSwipe) {
        setVisibility(toInvisible, View.INVISIBLE);
        centerIcons(iconsToCenter, centerIcon);
        animateActionText(textToCenter, rightSwipe);
    }

    private void centerIcons(View[] icons, View centerIcon) {
        int toMove = getToCenterDistance(centerIcon);
        TranslateAnimation centerAnimation = getCenterIconAnimation(toMove);
        for (View view : icons) {
            view.startAnimation(centerAnimation);
        }
    }

    private void animateActionText(TextView textView, boolean swipeRight) {
        int startTextPosition = getOnScreenX(longSwipeLayout) - getOnScreenX(textView);
        int endTextPosition = (longSwipeLayout.getWidth() / 2) - textView.getWidth() / 2;
        if (swipeRight)
            startTextPosition += longSwipeLayout.getWidth();
        else
            endTextPosition = -endTextPosition;
        animateActionText(textView, startTextPosition, endTextPosition);
    }

    private int getToCenterDistance(View centerView) {
        int center = getOnScreenX(longSwipeLayout) + (shotImageView.getWidth() / 2) - (centerView.getWidth() / 2);
        return center - getOnScreenX(centerView);
    }

    private void handleLeftSwipe(float progress, int swipePosition) {
        handleLikeAnimation(swipePosition, progress);
        handlePlusAndBucketAnimation(swipePosition);
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

    private void handleFollowIconSwipe(float progress) {
        if (followIcon != null) {
            animateAlpha(followIcon, FOLLOW_ICON_ALPHA_ANIMATION_START_PROGRESS,
                    FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS, progress);
            animateAlpha(followIconFull, FOLLOW_ICON_FILLED_ALPHA_ANIMATION_START_PROGRESS,
                    FOLLOW_ICON_ALPHA_ANIMATION_END_PROGRESS, progress);
            followIcon.setX(commentIcon.getX());
            followIconFull.setX(commentIcon.getX());
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

    private void fadeLikeAnimation() {
        AlphaAnimation fadeAnimation = new AlphaAnimation(ALPHA_MAX, ALPHA_MIN);
        fadeAnimation.setFillAfter(true);
        fadeAnimation.setDuration(LIKE_FADE_ANIMATION_DURATION);
        if (plusIcon != null)
            plusIcon.startAnimation(fadeAnimation);
        if (bucketIcon != null)
            bucketIcon.startAnimation(fadeAnimation);
    }

    private void finishAction(View[] toClearAnimation,
                              View[] toVisible,
                              int delay1,
                              int delay2,
                              FinishSwipeActionListener finishSwipeActionListener) {
        longSwipeLayout.postDelayed(() -> {
            longSwipeLayout.close();
            shotImageView.clearAnimation();

            longSwipeLayout.postDelayed(() -> {
                clearAnimations(toClearAnimation);
                if (finishSwipeActionListener != null)
                    finishSwipeActionListener.finishSwipeAction();
                setVisibility(toVisible, View.VISIBLE);
            }, delay1);
        }, delay2);
    }

    private void setVisibility(View[] views, int visibility) {
        for (View view : views)
            if (view != null)
                view.setVisibility(visibility);
    }

    private void clearAnimations(View[] views) {
        for (View view : views)
            if (view != null)
                view.clearAnimation();
    }

    private TranslateAnimation getCenterIconAnimation(int toMove) {
        TranslateAnimation centerImageViewTranslateAnimation = new TranslateAnimation(0, toMove, 0, 0);
        centerImageViewTranslateAnimation.setDuration(ICON_CENTER_ANIMATION_DURATION);
        centerImageViewTranslateAnimation.setFillAfter(true);
        return centerImageViewTranslateAnimation;
    }

    private void animateActionText(TextView textView, int startTextPosition, int endTextPosition) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(TEXT_CENTER_ANIMATION_DURATION);
        animationSet.addAnimation(new TranslateAnimation(startTextPosition, endTextPosition, 0, 0));
        animationSet.addAnimation(new AlphaAnimation(ALPHA_MIN, ALPHA_MAX));
        textView.startAnimation(animationSet);
    }

    private int getOnScreenX(View view) {
        int[] coordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        view.getLocationOnScreen(coordinates);
        return coordinates[0];
    }

    @FunctionalInterface
    protected interface FinishSwipeActionListener {

        void finishSwipeAction();
    }
}
