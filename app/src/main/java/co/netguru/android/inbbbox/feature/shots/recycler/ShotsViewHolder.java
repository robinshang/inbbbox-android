package co.netguru.android.inbbbox.feature.shots.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.SwipeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.utils.imageloader.ImageLoader;

public class ShotsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_shot_image)
    ImageView shotImageView;

    @BindView(R.id.swipe_layout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottom_wrapper)
    View bottomWrapper;

    private ImageLoader imageLoader;

    ShotsViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
        ButterKnife.bind(this, itemView);
        initSwipeLayout();
    }

    private void initSwipeLayout() {
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomWrapper);
        swipeLayout.addSwipeListener(new SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
                swipeLayout.close(true);
            }
        });
    }

    void bind(Shot shot) {
        setupImage(shot);
    }

    private void setupImage(Shot shot) {
        float radius = itemView.getContext().getResources().getDimension(R.dimen.shot_corner_radius);
        imageLoader.enableRoundCornersTransformationForNextRequest(radius);
        imageLoader.loadImageWithThumbnail(shotImageView, shot);
    }
}
