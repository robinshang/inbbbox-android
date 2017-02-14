package co.netguru.android.inbbbox.feature.shot.recycler;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    ImageView likeIcon;
    @BindView(R.id.iv_plus_image)
    ImageView plusIcon;
    @BindView(R.id.iv_bucket_action)
    ImageView bucketIcon;
    @BindView(R.id.iv_comment)
    ImageView commentIcon;
    @BindView(R.id.iv_follow)
    ImageView followIcon;
    @BindView(R.id._left_wrapper)
    LinearLayout leftWrapper;
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
    public void onLeftSwipeActivate(boolean isActive) {
        if (!shot.isLiked()) {
            likeIcon.setActivated(isActive);
        }
    }

    @Override
    public void onLeftLongSwipeActivate(boolean isActive) {
        bucketIcon.setActivated(isActive);
    }

    @Override
    public void onRightSwipeActivate(boolean isActive) {
        commentIcon.setActivated(isActive);
    }

    @Override
    public void onRightLongSwipeActivate(boolean isActive) {
        followIcon.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
        commentIcon.setImageAlpha(isActive ? ALPHA_MIN : ALPHA_MAX);
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
        int progress = getSwipeProgress();
        handleLikeAnimation(progress);
        handlePlusAndBucketAnimation(progress);
    }

    private void handleLikeAnimation(int progress) {
        int likeIconLeft = likeIcon.getLeft();
        float likePercent = getPercent(progress, likeIconLeft, likeIcon.getWidth());

        translateView(likeIcon, progress, likePercent);
        scaleView(likeIcon, likePercent);
        handlePlusAndBucketAnimation(progress);
    }

    private void handlePlusAndBucketAnimation(int progress) {
        float plusPercent = getPercent(progress, plusIcon.getLeft(), plusIcon.getWidth());
        float bucketPercent = getPercent(progress, bucketIcon.getLeft(), bucketIcon.getWidth());

        scaleView(plusIcon, plusPercent);
        scaleView(bucketIcon, bucketPercent);
    }

    private void handleRightSwipe(int positionX, int swipeLimit) {
        float progress = (float) -positionX / (float) swipeLimit;
        float horizontalTranslation = positionX * COMMENT_TRANSLATION_FACTOR;

        commentIcon.setTranslationX(horizontalTranslation);
        scaleView(commentIcon, progress, commentIcon.getWidth(), commentIcon.getHeight() / 2f);
    }

    private void scaleView(View v, float percent) {
        scaleView(v, percent, 0, v.getHeight() / 2f);
    }

    private void scaleView(View v, float percent, float pivotX, float pivotY) {
        if (percent > 0 && percent < 1) {
            v.setPivotX(pivotX);
            v.setPivotY(pivotY);
            v.setScaleX(percent);
            v.setScaleY(percent);
        }
    }

    private void translateView(View v, int progress, float percent) {
        if (percent > 0 && percent < 1) {
            int horizontalTranslation = progress - v.getLeft() - ((int) (v.getWidth() * percent));
            v.setTranslationX(horizontalTranslation);
        }
    }

    private int getSwipeProgress() {
        int[] shotAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        shotImageView.getLocationOnScreen(shotAbsoluteCoordinates);

        int[] leftWrapperAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        leftWrapper.getLocationOnScreen(leftWrapperAbsoluteCoordinates);

        int diff = leftWrapperAbsoluteCoordinates[0] - leftWrapper.getLeft();
        return shotAbsoluteCoordinates[0] - diff;
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