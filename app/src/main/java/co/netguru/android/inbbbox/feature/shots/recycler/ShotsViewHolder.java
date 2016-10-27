package co.netguru.android.inbbbox.feature.shots.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;
import co.netguru.android.inbbbox.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.view.swipingpanel.SwipeableImagePanel;

public class ShotsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.swipeable_image_panel)
    SwipeableImagePanel imagePanel;

    private ImageLoader imageLoader;

    // TODO: 27.10.2016 bind with recycler action listener
    private ItemSwipeListener swipeListener = new ItemSwipeListener() {
        @Override
        public void onLeftSwipe() {
            Toast.makeText(imagePanel.getContext(), "Left swipie", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLeftLongSwipe() {
            Toast.makeText(imagePanel.getContext(), "Left LONG swipie", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRightSwipe() {
            Toast.makeText(imagePanel.getContext(), "Right swipie", Toast.LENGTH_SHORT).show();
        }
    };

    ShotsViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
        ButterKnife.bind(this, itemView);
    }

    void bind(Shot shot) {
        setupImage(shot);
        imagePanel.setItemSwipeListener(swipeListener);
    }

    private void setupImage(Shot shot) {
        float radius = itemView.getContext().getResources().getDimension(R.dimen.shot_corner_radius);
        imageLoader.enableRoundCornersTransformationForNextRequest(radius);
        imageLoader.loadImageWithThumbnail(imagePanel.getImageView(), shot);
    }
}

