package co.netguru.android.inbbbox.feature.shot.recycler;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindDrawable;
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

    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;
    private static final int LOCATION_ON_SCREEN_COORDINATES_NUMBER = 2;
    private static final float COMMENT_TRANSLATION_FACTOR = 1 / 3f;

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
    LinearLayout leftWrapper;
    @BindView(R.id.user_name_textView)
    TextView userNameTextView;
    @BindView(R.id.comments_count_textView)
    TextView commentsCountTextView;
    @BindView(R.id.likes_count_textView)
    TextView likesCountTextView;

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

    private void setupImage(Shot shot) {
        shotImageView.loadShot(shot);
        likeIconImageView.setActivated(shot.isLiked());
    }

    private void setupDetails(Shot shot) {
        if (shot.author() != null && shot.author().username() != null)
            userNameTextView.setText(shot.author().username());

        likesCountTextView.setText(String.valueOf(shot.likesCount()));
        commentsCountTextView.setText(String.valueOf(shot.commentsCount()));
    }
}