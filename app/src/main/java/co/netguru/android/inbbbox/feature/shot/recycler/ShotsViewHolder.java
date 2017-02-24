package co.netguru.android.inbbbox.feature.shot.recycler;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

class ShotsViewHolder extends BaseShotsViewHolder<Shot> implements DetailsVisibilityChangeListener {

    public static final int ALPHA_MAX = 255;
    public static final int ALPHA_MIN = 0;
    public static final int LOCATION_ON_SCREEN_COORDINATES_NUMBER = 2;
    public static final float COMMENT_TRANSLATION_FACTOR = 1 / 3f;

    private final ShotSwipeListener shotSwipeListener;
    @BindView(R.id.user_name_textView)
    TextView userNameTextView;
    @BindView(R.id.comments_count_textView)
    TextView commentsCountTextView;
    @BindView(R.id.likes_count_textView)
    TextView likesCountTextView;
    @BindView(R.id.user_imageView)
    ImageView userImageView;
    @BindView(R.id.shot_details_layout)
    View shotDetailsView;
    @BindDrawable(R.drawable.shot_placeholder)
    Drawable shotPlaceHolder;

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
    //additional TextViews needed due to closing SwipeLayout when text changes
    @BindView(R.id.like_done_text)
    TextView likeDoneText;
    @BindView(R.id.add_to_bucket_done_text)
    TextView bucketDoneText;
    @BindView(R.id.follow_done_text)
    TextView followDoneText;
    @BindView(R.id.comment_done_text)
    TextView commentDoneText;

    private Shot shot;

    ShotsViewHolder(View itemView, @NonNull ShotSwipeListener shotSwipeListener,
                    @NonNull DetailsVisibilityChangeEmitter emitter) {
        super(itemView);
        this.shotSwipeListener = shotSwipeListener;
        emitter.setListener(this);
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
        animateShotView(false);
        animateAndFinishLike(likeDoneText, likeIconImageView);
    }

    @Override
    public void onLeftLongSwipe() {
        animateShotView(false);
        animateAndFinishAddToBucket(bucketDoneText, plusIconImageView);
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
    public void onStartSwipe() {
        shotSwipeListener.onStartSwipe(shot);
    }

    @Override
    public void onEndSwipe() {
        shotSwipeListener.onEndSwipe(shot);
    }

    @Override
    public void onDetailsChangeVisibility(boolean shouldBeVisible) {
        if (shouldBeVisible)
            shotDetailsView.setVisibility(View.VISIBLE);
        else
            shotDetailsView.setVisibility(View.INVISIBLE);
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
        int endTextPosition = halfShotWidth - textView.getWidth() / 2;
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
        int endTextPosition = halfShotWidth - textView.getWidth() / 2;
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
                shotSwipeListener.onLikeAndAddShotToBucketSwipe(shot);
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
        int endTextPosition = halfShotWidth - textView.getWidth() / 2;
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
        int endTextPosition = halfShotWidth - textView.getWidth() / 2;
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
