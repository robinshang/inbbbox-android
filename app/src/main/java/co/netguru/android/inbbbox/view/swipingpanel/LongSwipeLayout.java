package co.netguru.android.inbbbox.view.swipingpanel;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;

import timber.log.Timber;

public class LongSwipeLayout extends SwipeLayout {

    private static final long AUTO_CLOSE_DELAY = 300;
    private static final float LONG_SWIPE_TRIGGERING_THRESHOLD = 200;
    private View surfaceView;

    private int initialPadding;
    private float offsetX;
    private boolean wasChecked = false;
    private boolean isFirstItemSelectionTriggered;
    private int firstElementEndThreshold;
    private boolean wasFirstElementWidthCollected = false;
    private ItemSwipeListener itemSwipeListener;

    private final Handler closeHandler = new Handler();

    private SwipeListener internalSwipeListener = new SwipeListener() {
        @Override
        public void onStartOpen(SwipeLayout layout) {

        }

        @Override
        public void onOpen(SwipeLayout layout) {

        }

        @Override
        public void onStartClose(SwipeLayout layout) {

        }

        @Override
        public void onClose(SwipeLayout layout) {

        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

        }

        @Override
        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
            delayClose();
        }
    };

    public LongSwipeLayout(Context context) {
        super(context);
        init();
    }

    public LongSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LongSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        surfaceView = getSurfaceView();
        initialPadding = surfaceView.getLeft();
    }

    private void init() {
        setShowMode(SwipeLayout.ShowMode.LayDown);
        addSwipeListener(internalSwipeListener);
    }

    private void getElementWidth() {
        firstElementEndThreshold = (getPaddingLeft() - getDragDistance());
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            wasChecked = false;
            isFirstItemSelectionTriggered = false;
            if (!wasFirstElementWidthCollected) {
                getElementWidth();
                wasFirstElementWidthCollected = true;
            }
        }

        int difference = initialPadding - surfaceView.getLeft();

        if (difference < firstElementEndThreshold && !wasChecked) {
            offsetX = event.getX();
            wasChecked = true;
            isFirstItemSelectionTriggered = true;
        }
        if (event.getX() - offsetX > LONG_SWIPE_TRIGGERING_THRESHOLD) {
            isFirstItemSelectionTriggered = false;
        }
        if (isFirstItemSelectionTriggered) {
            event.setLocation(offsetX, event.getY());
        } else if (wasChecked) {

            float offset = event.getX() - LONG_SWIPE_TRIGGERING_THRESHOLD;
            event.setLocation(offset, event.getY());
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            checkItemSelection();
        }

        return super.onTouchEvent(event);
    }

    private void checkItemSelection() {
        if (isFirstItemSelectionTriggered) {
            normalSwipeElementSelected();
        } else {
            longSwipeElementSelected();
        }
    }

    private void normalSwipeElementSelected() {
        Timber.d("Normal swipe item selected");
        if (itemSwipeListener != null) {
            itemSwipeListener.onLeftSwipe();
        }
    }

    private void longSwipeElementSelected() {
        Timber.d("Long swipe item selected");
        if (itemSwipeListener != null) {
            itemSwipeListener.onLeftLongSwipe();
        }
    }

    public void setItemSwipeListener(ItemSwipeListener itemSwipeListener) {

        this.itemSwipeListener = itemSwipeListener;
    }

    @Override
    protected void processHandRelease(float xvel, float yvel, boolean isCloseBeforeDragged) {
        //keep it false to automatically close the surface
        super.processHandRelease(xvel, yvel, false);
    }

    private void delayClose() {
        closeHandler.postDelayed(() -> close(true, true), AUTO_CLOSE_DELAY);
    }
}
