package co.netguru.android.inbbbox.feature.shots.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;
import co.netguru.android.inbbbox.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.view.swipingpanel.LongSwipeLayout;

public class ShotsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.long_swipe_layout)
    LongSwipeLayout longSwipeLayout;

    @BindView(R.id.iv_shot_image)
    ImageView shotImageView;

    @BindView(R.id.iv_background)
    ImageView backgroundImageView;

    private ImageLoader imageLoader;

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

    ShotsViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
        ButterKnife.bind(this, itemView);
    }

    void bind(Shot shot) {
        setupImage(shot);
        longSwipeLayout.setItemSwipeListener(swipeListener);
    }

    private void setupImage(Shot shot) {
        imageLoader.loadImageFromResourcesWithRoundedCorners(R.drawable.shot_placeholder,
                R.dimen.shot_corner_radius,
                backgroundImageView);
        imageLoader.loadImageWithThumbnail(shotImageView, shot);
    }

}

