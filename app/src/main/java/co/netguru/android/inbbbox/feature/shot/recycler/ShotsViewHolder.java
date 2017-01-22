package co.netguru.android.inbbbox.feature.shot.recycler;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        followImageView.setActivated(isActive);
    }

    @Override
    public void onSwipeProgress() {
        int[] shotAbsoluteCoordinates = new int[2];
        shotImageView.getLocationOnScreen(shotAbsoluteCoordinates);
        int[] leftWrapperAbsoluteCoordinates = new int[2];
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

    private float getPercent(int progress, int viewLeft, int viewWidth) {
        int percent = 0;
        if (viewLeft != 0 || viewWidth != 0) {
            percent = 100 * progress / (viewLeft + viewWidth);
        }
        return percent / 100f;
    }
}