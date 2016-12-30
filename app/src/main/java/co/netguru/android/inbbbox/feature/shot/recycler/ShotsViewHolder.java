package co.netguru.android.inbbbox.feature.shot.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

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

    @BindView(R.id.gif_label_textView)
    View gifLabelView;

    @BindView(R.id.iv_comment)
    ImageView commentImageView;

    private final ShotSwipeListener shotSwipeListener;

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
        float radius = itemView.getResources().getDimension(R.dimen.shot_corner_radius);
        final Context context = itemView.getContext();
        backgroundImageView.setRadius(radius);
        shotImageView.setRadius(radius);

        ShotLoadingUtil.loadListShot(context, shotImageView, shot);

        if (shot.isGif()) {
            gifLabelView.setVisibility(View.VISIBLE);
        } else {
            gifLabelView.setVisibility(View.GONE);
        }

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