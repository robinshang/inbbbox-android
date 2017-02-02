package co.netguru.android.inbbbox.feature.shot.recycler;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

class ShotsViewHolder extends BaseViewHolder<Shot>
        implements ItemSwipeListener {

    public static final int ALPHA_MAX = 255;
    public static final int ALPHA_MIN = 0;
    public static final int LOCATION_ON_SCREEN_COORDINATES_NUMBER = 2;
    public static final float COMMENT_TRANSLATION_FACTOR = 1 / 3f;

    private final ShotSwipeListener shotSwipeListener;
    @BindView(R.id.long_swipe_layout)
    LongSwipeLayout longSwipeLayout;
    @BindView(R.id.iv_shot_image)
    RoundedCornersShotImageView shotImageView;
    @BindView(R.id.iv_like_action)
    ImageView likeIconImageView;
    @BindView(R.id.iv_plus_image)
    ImageView plusIconImageView;
    @BindView(R.id.iv_bucket_action)
    ImageView bucketImageView;
    @BindView(R.id.iv_comment)
    ImageView commentImageView;
    @BindView(R.id.iv_follow)
    ImageView followImageView;
    @BindView(R.id._left_wrapper)
    RelativeLayout leftWrapper;
    @BindView(R.id.like_done_text)
    TextView likeDoneText;
    @BindView(R.id.add_to_bucket_done_text)
    TextView bucketDoneText;
    @BindView(R.id.follow_done_text)
    TextView followDoneText;
    @BindView(R.id.comment_done_text)
    TextView commentDoneText;
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
    }

    private void setupImage(Shot shot) {
        shotImageView.loadShot(shot);
        likeIconImageView.setActivated(shot.isLiked());
    }

    @Override
    public void onLeftSwipe() {
        animateShotView(false);
        animateAndFinishLike(likeDoneText, likeIconImageView);

    }

    @Override
    public void onLeftLongSwipe() {
        animateShotView(false);
        animateAndFinishAddToBucket(bucketDoneText, bucketImageView);
    }

    @Override
    public void onRightSwipe() {
        animateShotView(true);
        animateAndFinishComment(commentDoneText, commentImageView);

    }

    @Override
    public void onRightLongSwipe() {
        animateShotView(true);
        animateAndFinishFollowUser(followDoneText, followImageView);
    }

    @Override
    public void onLeftSwipeActivate(boolean isActive) {
        if (!shot.isLiked()) {
            likeIconImageView.setActivated(isActive);
        }
    }

    @Override
    public void onLeftLongSwipeActivate(boolean isActive) {
        bucketImageView.setActivated(isActive);
    }

    @Override
    public void onRightSwipeActivate(boolean isActive) {
        commentImageView.setActivated(isActive);
    }

    @Override
    public void onRightLongSwipeActivate(boolean isActive) {
        followImageView.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
        commentImageView.setImageAlpha(isActive ? ALPHA_MIN : ALPHA_MAX);
    }

    @Override
    public void onSwipeProgress(int positionX, int swipeLimit) {
        if (positionX > 0) {
            handleLeftSwipe();
        } else {
            handleRightSwipe(positionX, swipeLimit);
        }
    }

    private void handleLeftSwipe() {
        int[] shotAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        shotImageView.getLocationOnScreen(shotAbsoluteCoordinates);
        int[] leftWrapperAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        leftWrapper.getLocationOnScreen(leftWrapperAbsoluteCoordinates);
        int diff = leftWrapperAbsoluteCoordinates[0] - leftWrapper.getLeft();
        int progress = shotAbsoluteCoordinates[0] - diff;
        int likeIconImageViewLeft = likeIconImageView.getLeft();

        float percent = getPercent(progress, likeIconImageViewLeft, likeIconImageView.getWidth());
        float plusPercent = getPercent(progress, plusIconImageView.getLeft(), plusIconImageView.getWidth());
        float bucketPercent = getPercent(progress, bucketImageView.getLeft(), bucketImageView.getWidth());

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);

        if (percent <= 1) {
            int horizontalTranslation = progress - likeIconImageViewLeft - ((int) (likeIconImageView.getWidth() * percent));
            animationSet.addAnimation(new ScaleAnimation(percent, percent, percent, percent, 0, likeIconImageView.getHeight() / 2));
            animationSet.addAnimation(new TranslateAnimation(horizontalTranslation, horizontalTranslation, 0, 0));
            likeIconImageView.startAnimation(animationSet);
        } else if (plusPercent <= 1) {
            //in case of fast swipe
            likeIconImageView.clearAnimation();

            animationSet.addAnimation(new ScaleAnimation(plusPercent, plusPercent, plusPercent, plusPercent, 0, plusIconImageView.getHeight() / 2));
            plusIconImageView.startAnimation(animationSet);
        } else if (bucketPercent <= 1) {
            //in case of fast swipe
            plusIconImageView.clearAnimation();

            animationSet.addAnimation(new ScaleAnimation(bucketPercent, bucketPercent, bucketPercent, bucketPercent, 0, bucketImageView.getHeight() / 2));
            bucketImageView.startAnimation(animationSet);
        } else {
            //in case of fast swipe
            bucketImageView.clearAnimation();
        }
    }

    private void handleRightSwipe(int positionX, int swipeLimit) {
        float progress = (float) -positionX / (float) swipeLimit;

        float horizontalTranslation = positionX * COMMENT_TRANSLATION_FACTOR;
        commentImageView.setTranslationX(horizontalTranslation);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(new ScaleAnimation(progress, progress, progress, progress,
                commentImageView.getWidth(), commentImageView.getHeight() / 2));
        commentImageView.startAnimation(animationSet);
    }

    private float getPercent(int progress, int viewLeft, int viewWidth) {
        int percent = 0;
        if (viewLeft != 0 || viewWidth != 0) {
            percent = 100 * progress / (viewLeft + viewWidth);
        }
        return percent / 100f;
    }

    private void animateShotView(boolean rightToLeft) {
        int finishLocation = rightToLeft ? -shotImageView.getWidth() : shotImageView.getWidth();
        TranslateAnimation translateAnimation = new TranslateAnimation(0, finishLocation, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(1000);
        shotImageView.startAnimation(translateAnimation);
    }

    private int getOnScreenX(View view) {
        int[] coordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        view.getLocationOnScreen(coordinates);
        return coordinates[0];
    }

    private void animateAndFinishLike(TextView textView, ImageView centerImageView) {
        int leftWrapperOnScreenX = getOnScreenX(leftWrapper);
        int centerImageOnScreenX = getOnScreenX(centerImageView);
        int textOnScreenX = getOnScreenX(textView);

        int halfShotWidth = shotImageView.getWidth() / 2;
        int center = leftWrapperOnScreenX + halfShotWidth - centerImageView.getWidth() / 2;
        int toMove = center - centerImageOnScreenX;

        AlphaAnimation fadeAnimation = new AlphaAnimation(1f, 0f);
        fadeAnimation.setFillAfter(true);
        fadeAnimation.setDuration(50);

        plusIconImageView.startAnimation(fadeAnimation);
        bucketImageView.startAnimation(fadeAnimation);

        TranslateAnimation centerImageViewTranslateAnimation = new TranslateAnimation(0, toMove, 0, 0);
        centerImageViewTranslateAnimation.setDuration(1000);
        centerImageViewTranslateAnimation.setFillAfter(true);
        centerImageView.startAnimation(centerImageViewTranslateAnimation);

        int startTextPosition = leftWrapperOnScreenX - textOnScreenX + leftWrapper.getWidth();
        int endTextPosition = halfShotWidth - (int) (textView.getPaint().measureText(textView.getText().toString()) / 2);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(1000);
        animationSet.addAnimation(new TranslateAnimation(startTextPosition, endTextPosition, 0, 0));
        animationSet.addAnimation(new AlphaAnimation(0f, 1f));
        textView.startAnimation(animationSet);
        longSwipeLayout.postDelayed(() -> {
            longSwipeLayout.close();
            shotImageView.clearAnimation();
            shotSwipeListener.onShotLikeSwipe(shot);

            longSwipeLayout.postDelayed(() -> {
                centerImageView.clearAnimation();
                textView.clearAnimation();
                plusIconImageView.clearAnimation();
                bucketImageView.clearAnimation();
            }, 200);

        }, 1200);
    }

    private void animateAndFinishAddToBucket(TextView textView, ImageView centerImageView) {
        int leftWrapperOnScreenX = getOnScreenX(leftWrapper);
        int centerImageOnScreenX = getOnScreenX(centerImageView);
        int textOnScreenX = getOnScreenX(textView);

        int halfShotWidth = shotImageView.getWidth() / 2;
        int center = leftWrapperOnScreenX + halfShotWidth - centerImageView.getWidth() / 2;
        int toMove = center - centerImageOnScreenX;

        TranslateAnimation centerImageViewTranslateAnimation = new TranslateAnimation(0, toMove, 0, 0);
        centerImageViewTranslateAnimation.setDuration(1000);
        centerImageViewTranslateAnimation.setFillAfter(true);
        centerImageView.startAnimation(centerImageViewTranslateAnimation);

        bucketImageView.startAnimation(centerImageViewTranslateAnimation);
        likeIconImageView.startAnimation(centerImageViewTranslateAnimation);

        int startTextPosition = leftWrapperOnScreenX - textOnScreenX + leftWrapper.getWidth();
        int endTextPosition = halfShotWidth - (int) (textView.getPaint().measureText(textView.getText().toString()) / 2);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(1000);
        animationSet.addAnimation(new TranslateAnimation(startTextPosition, endTextPosition, 0, 0));
        animationSet.addAnimation(new AlphaAnimation(0f, 1f));
        textView.startAnimation(animationSet);
        longSwipeLayout.postDelayed(() -> {
            longSwipeLayout.close();
            shotImageView.clearAnimation();

            longSwipeLayout.postDelayed(() -> {
                likeIconImageView.clearAnimation();
                textView.clearAnimation();
                centerImageView.clearAnimation();
                bucketImageView.clearAnimation();
                shotSwipeListener.onAddShotToBucketSwipe(shot);
            }, 200);

        }, 1200);
    }

    private void animateAndFinishFollowUser(TextView textView, ImageView centerImageView) {
        int leftWrapperOnScreenX = getOnScreenX(leftWrapper);
        int centerImageOnScreenX = getOnScreenX(centerImageView);
        int textOnScreenX = getOnScreenX(textView);

        int halfShotWidth = shotImageView.getWidth() / 2;
        int center = leftWrapperOnScreenX + halfShotWidth - centerImageView.getWidth() / 2;
        int toMove = center - centerImageOnScreenX;

        TranslateAnimation centerImageViewTranslateAnimation = new TranslateAnimation(0, toMove, 0, 0);
        centerImageViewTranslateAnimation.setDuration(1000);
        centerImageViewTranslateAnimation.setFillAfter(true);
        centerImageView.startAnimation(centerImageViewTranslateAnimation);

        int startTextPosition = leftWrapperOnScreenX - textOnScreenX;
        int endTextPosition = halfShotWidth - (int) (textView.getPaint().measureText(textView.getText().toString()) / 2);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(1000);
        animationSet.addAnimation(new TranslateAnimation(startTextPosition, -endTextPosition, 0, 0));
        animationSet.addAnimation(new AlphaAnimation(0f, 1f));
        textView.startAnimation(animationSet);

        longSwipeLayout.postDelayed(() -> {
            longSwipeLayout.close();
            shotImageView.clearAnimation();

            longSwipeLayout.postDelayed(() -> {
                textView.clearAnimation();
                centerImageView.clearAnimation();
                shotSwipeListener.onFollowUserSwipe(shot);
            }, 200);

        }, 1200);
    }

    private void animateAndFinishComment(TextView textView, ImageView centerImageView) {
        int leftWrapperOnScreenX = getOnScreenX(leftWrapper);
        int centerImageOnScreenX = getOnScreenX(centerImageView);
        int textOnScreenX = getOnScreenX(textView);

        int halfShotWidth = shotImageView.getWidth() / 2;
        int center = leftWrapperOnScreenX + halfShotWidth - centerImageView.getWidth() / 2;
        int toMove = center - centerImageOnScreenX;

        TranslateAnimation centerImageViewTranslateAnimation = new TranslateAnimation(0, toMove, 0, 0);
        centerImageViewTranslateAnimation.setDuration(1000);
        centerImageViewTranslateAnimation.setFillAfter(true);
        centerImageView.startAnimation(centerImageViewTranslateAnimation);

        int startTextPosition = leftWrapperOnScreenX - textOnScreenX;
        int endTextPosition = halfShotWidth - (int) (textView.getPaint().measureText(textView.getText().toString()) / 2);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(1000);
        animationSet.addAnimation(new TranslateAnimation(startTextPosition, -endTextPosition, 0, 0));
        animationSet.addAnimation(new AlphaAnimation(0f, 1f));
        textView.startAnimation(animationSet);

        longSwipeLayout.postDelayed(() -> {
            longSwipeLayout.close();
            shotImageView.clearAnimation();

            longSwipeLayout.postDelayed(() -> {
                textView.clearAnimation();
                centerImageView.clearAnimation();
                shotSwipeListener.onCommentShotSwipe(shot);
            }, 300);

        }, 1200);
    }
}