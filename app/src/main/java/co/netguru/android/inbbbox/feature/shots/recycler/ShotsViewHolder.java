package co.netguru.android.inbbbox.feature.shots.recycler;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindDrawable;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.view.swipingpanel.LongSwipeLayout;

public class ShotsViewHolder extends BaseViewHolder<Shot> {

    @BindView(R.id.long_swipe_layout)
    LongSwipeLayout longSwipeLayout;

    @BindView(R.id.iv_shot_image)
    ImageView shotImageView;
    @BindView(R.id.iv_like_action)
    ImageView likeIconImageView;

    @BindDrawable(R.drawable.ic_like_swipe_filled)
    Drawable shotLikedIcon;
    @BindDrawable(R.drawable.ic_like_swipe)
    Drawable shotUnlikedIcon;

    private OnShotLeftSwipeListener onLeftSwipeListener = OnShotLeftSwipeListener.NULL;

    // TODO: 27.10.2016 bind with recycler action listener
    private ItemSwipeListener swipeListener = new ItemSwipeListener() {
        @Override
        public void onLeftSwipe() {
            onLeftSwipeListener.onLeftSwipe(getAdapterPosition());
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

    ShotsViewHolder(View itemView, OnShotLeftSwipeListener onLeftSwipeListener) {
        super(itemView);
        this.onLeftSwipeListener = onLeftSwipeListener == null
                ? OnShotLeftSwipeListener.NULL : onLeftSwipeListener;
    }

    @Override
    public void bind(Shot shot) {
        setupImage(shot);
        longSwipeLayout.setItemSwipeListener(swipeListener);
    }

    private void setupImage(Shot shot) {
        Glide.with(itemView.getContext())
                .load(shot.normalImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.shot_item_swipe_bottom_background)
                .into(shotImageView);

        likeIconImageView.setImageDrawable(shot.likeStatus() == Shot.LIKED ? shotLikedIcon : shotUnlikedIcon);
    }

    public interface OnShotLeftSwipeListener {

        OnShotLeftSwipeListener NULL = position -> {};

        void onLeftSwipe(int position);
    }
}

