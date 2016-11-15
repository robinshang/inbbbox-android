package co.netguru.android.inbbbox.feature.shots.recycler;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.OnClick;
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

    private ShotsAdapter.OnItemActionListener onShotActionListener = ShotsAdapter.OnItemActionListener.NULL;
    private Shot shot;

    ShotsViewHolder(View itemView, ShotsAdapter.OnItemActionListener onLeftSwipeListener) {
        super(itemView);
        this.onShotActionListener = onLeftSwipeListener == null
                ? ShotsAdapter.OnItemActionListener.NULL : onLeftSwipeListener;
    }

    @OnClick(R.id.iv_shot_image)
    void onShotClick(){
        onShotActionListener.onItemClicked(getAdapterPosition());
    }

    @Override
    public void bind(Shot shot) {
        this.shot = shot;
        setupImage(shot);
        longSwipeLayout.setItemSwipeListener(this);
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
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .animate(android.R.anim.fade_in)
                .into(shotImageView);

        likeIconImageView.setActivated(shot.isLiked());
    }

    @Override
    public void onLeftSwipe() {
        onShotActionListener.onItemLeftSwipe(getAdapterPosition());
        // TODO: 03.11.2016 remove toasts or change to propare info after all action implemented
        Toast.makeText(longSwipeLayout.getContext(), "Left swipie", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftLongSwipe() {
        // TODO: 03.11.2016 remove toasts or change to propare info after all action implemented
        Toast.makeText(longSwipeLayout.getContext(), "Left LONG swipie", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightSwipe() {
        // TODO: 03.11.2016 remove toasts or change to propare info after all action implemented
        Toast.makeText(longSwipeLayout.getContext(), "Right swipie", Toast.LENGTH_SHORT).show();
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

