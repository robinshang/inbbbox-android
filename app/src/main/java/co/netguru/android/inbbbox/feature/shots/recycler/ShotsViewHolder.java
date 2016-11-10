package co.netguru.android.inbbbox.feature.shots.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.ThumbnailUtil;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;
import co.netguru.android.inbbbox.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.view.swipingpanel.LongSwipeLayout;

class ShotsViewHolder extends BaseViewHolder<Shot>
        implements ItemSwipeListener {

    @BindView(R.id.long_swipe_layout)
    LongSwipeLayout longSwipeLayout;

    @BindView(R.id.iv_shot_image)
    RoundedCornersImageView shotImageView;

    @BindView(R.id.iv_background)
    RoundedCornersImageView backgroundImageView;

    @BindView(R.id.iv_like_action)
    ImageView likeIconImageView;

    @BindView(R.id.iv_bucket_action)
    ImageView bucketImageView;

    @BindView(R.id.iv_comment)
    ImageView commentImageView;

    private final ShotSwipeListener shotSwipeListener;

    private Shot shot;

    ShotsViewHolder(View itemView, @NonNull ShotSwipeListener shotSwipeListener) {
        super(itemView);
        this.shotSwipeListener = shotSwipeListener;
        longSwipeLayout.setItemSwipeListener(this);
    }

    @Override
    public void bind(Shot shot) {
        this.shot = shot;
        setupImage(shot);
    }

    private void setupImage(Shot shot) {
        float radius = itemView.getResources().getDimension(R.dimen.shot_corner_radius);
        final Context context = itemView.getContext();
        backgroundImageView.setRadius(radius);
        shotImageView.setRadius(radius);
        Glide.with(context)
                .load(shot.normalImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .thumbnail(ThumbnailUtil.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(shotImageView);

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
}