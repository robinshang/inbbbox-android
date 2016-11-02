package co.netguru.android.inbbbox.feature.shots.recycler;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.utils.ThumbnailHelper;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;
import co.netguru.android.inbbbox.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.view.swipingpanel.LongSwipeLayout;

public class ShotsViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.long_swipe_layout)
    LongSwipeLayout longSwipeLayout;

    @BindView(R.id.iv_shot_image)
    RoundedCornersImageView shotImageView;

    @BindView(R.id.iv_background)
    RoundedCornersImageView backgroundImageView;

    // TODO: 27.10.2016 bind with recycler action listener
    private ItemSwipeListener swipeListener = new ItemSwipeListener() {
        @Override
        public void onLeftSwipe() {
            Toast.makeText(longSwipeLayout.getContext(), "Left swipie", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLeftLongSwipe() {
            Toast.makeText(longSwipeLayout.getContext(), "Left LONG swipie", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRightSwipe() {
            Toast.makeText(longSwipeLayout.getContext(), "Right swipie", Toast.LENGTH_SHORT).show();
        }
    };

    ShotsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Shot shot) {
        setupImage(shot);
        longSwipeLayout.setItemSwipeListener(swipeListener);
    }

    private void setupImage(Shot shot) {
        float radius = itemView.getResources().getDimension(R.dimen.shot_corner_radius);
        Context context = itemView.getContext();
        backgroundImageView.setRadius(radius);
        shotImageView.setRadius(radius);
        Glide.with(context)
                .load(shot.normalImageUrl())
                .placeholder(R.drawable.shot_placeholder)
                .thumbnail(ThumbnailHelper.getThumbnailRequest(context, shot.thumbnailUrl()))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .animate(android.R.anim.fade_in)
                .into(shotImageView);
    }

}

