package co.netguru.android.inbbbox.view;

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
    SwipeLayout swipeLayout;

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

            int max = 640;

            if (leftOffset > (640 / 2) - 80 && leftOffset < (640 / 2)) {
                Timber.d("TRIGGERED--------->");
                layout.setRightSwipeEnabled(false);
            } else {
                layout.setRightSwipeEnabled(true);
                Timber.d("leftOffset: " + leftOffset);
            }
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
            delayClose();
        }
    };

    private float initialPosition;
    private float rightPanelwidth;

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

        rightPanelwidth = getContext().getResources().getDimensionPixelOffset(R.dimen.right_swipe_action_panel_width);
        setupSwipeView();
    }

    private void setupSwipeView() {
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomWrapper);
        swipeLayout.addSwipeListener(swipeListener);
//        swipeLayout.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                boolean result = false;
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    initialPosition = event.getY();
//
//                }
//
//                Timber.d("Difference: " + Float.toString(event.getY()) + " /" + Float.toString(rightPanelwidth / 2));
//                if (initialPosition - event.getY() > rightPanelwidth / 2) {
//                    Timber.d("TRIGGERED!");
//                }
//
//                return result;
//            }
//        });
    }

    private void delayClose() {
        closeHandler.postDelayed(() -> swipeLayout.close(true, true), AUTO_CLOSE_DELAY);
    }

    public ImageView getImageView() {
        return shotImageView;
    }
}
