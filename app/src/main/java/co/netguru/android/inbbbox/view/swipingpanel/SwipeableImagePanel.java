package co.netguru.android.inbbbox.view.swipingpanel;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class SwipeableImagePanel extends LinearLayout {

    private static final long AUTO_CLOSE_DELAY = 600;

    @BindView(R.id.iv_shot_image)
    ImageView shotImageView;

    @BindView(R.id.swipe_layout)
    LongSwipeLayout swipeLayout;

    @BindView(R.id.bottom_wrapper)
    View bottomWrapper;

    private View rootView;
    private Handler closeHandler = new Handler();
    private SwipeLayout.SwipeListener swipeListener = new SwipeLayout.SwipeListener() {
        @Override
        public void onClose(SwipeLayout layout) {
            //when the SurfaceView totally cover the BottomView.
        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
            //you are swiping.
            Timber.d("LEFT OFFSET: " + leftOffset);
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
            delayClose();
        }
    };
    private ItemSwipeListener itemSwipeListener;

    public SwipeableImagePanel(Context context) {
        super(context);
        initSwipableView();
    }

    public SwipeableImagePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSwipableView();
    }

    public SwipeableImagePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSwipableView();
    }

    private void initSwipableView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.swipeable_image_panel_layout, this, true);
        ButterKnife.bind(rootView, this);

        setupSwipeView();
    }

    private void setupSwipeView() {
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomWrapper);
        swipeLayout.addSwipeListener(swipeListener);
    }

    private void delayClose() {
        closeHandler.postDelayed(() -> swipeLayout.close(true, true), AUTO_CLOSE_DELAY);
    }

    public ImageView getImageView() {
        return shotImageView;
    }

    public void setItemSwipeListener(ItemSwipeListener itemSwipeListener) {

        this.itemSwipeListener = itemSwipeListener;
        swipeLayout.setSwipeListener(itemSwipeListener);
    }
}

