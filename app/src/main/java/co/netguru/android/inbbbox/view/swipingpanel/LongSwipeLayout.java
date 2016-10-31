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
    private static final float LONG_SWIPE_TRIGGERING_THRESHOLD = 300;
    private static final int LONG_SWIPE_ACTIVATION_TOLERANCE = 100;
    private View surfaceView;

    private int initialPadding;
    private float offsetX;
    private boolean wasChecked = false;
    private boolean isNormalLeftSwipeTriggered;
    private int firstElementEndThreshold;
    private boolean wasFirstElementWidthCollected = false;
    private boolean isLongSwipeTriggered;
    private ItemSwipeListener itemSwipeListener;

    private final Handler closeHandler = new Handler();

    private boolean isRightSwipeTriggered;
    private final SwipeListener internalSwipeListener = new SwipeListener() {
        @Override
        public void onStartOpen(SwipeLayout layout) {
            //no-op
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            //no-op
        }

        @Override
        public void onStartClose(SwipeLayout layout) {
            //no-op
        }

        @Override
        public void onClose(SwipeLayout layout) {
            //no-op
        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
            handleRightSwipeAction(leftOffset);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initSwipeActionHandling();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            checkItemSelection();
        } else {
            handleSwipingActions(event);
        }
        return super.onTouchEvent(event);
    }

    public void setItemSwipeListener(ItemSwipeListener itemSwipeListener) {

        this.itemSwipeListener = itemSwipeListener;
    }

    @Override
    protected void processHandRelease(float xvel, float yvel, boolean isCloseBeforeDragged) {
        //keep it false to automatically close the surface
        super.processHandRelease(xvel, yvel, false);
    }

    private void init() {
        setShowMode(SwipeLayout.ShowMode.LayDown);
        addSwipeListener(internalSwipeListener);
    }

    private void getElementWidth() {
        if (!wasFirstElementWidthCollected) {
            wasFirstElementWidthCollected = true;
            firstElementEndThreshold = getLimitForLeftSwipe();
        }
    }

    private void handleSwipingActions(MotionEvent event) {
        int difference = initialPadding - surfaceView.getLeft();

        if (difference < firstElementEndThreshold && !wasChecked) {
            offsetX = event.getX();
            wasChecked = true;
            isNormalLeftSwipeTriggered = true;
            isLongSwipeTriggered = false;
        }
        if (event.getX() - offsetX > LONG_SWIPE_TRIGGERING_THRESHOLD) {
            isNormalLeftSwipeTriggered = false;
        }

        int swipingLimit = getLimitForLeftSwipe() + LONG_SWIPE_ACTIVATION_TOLERANCE;
        Timber.d("difference: " + difference + " limit: " + swipingLimit);
        if (difference < swipingLimit) {
            isLongSwipeTriggered = true;
        }

        if (isNormalLeftSwipeTriggered) {
            event.setLocation(offsetX, event.getY());
        } else if (wasChecked) {

            float offset = event.getX() - LONG_SWIPE_TRIGGERING_THRESHOLD;
            event.setLocation(offset, event.getY());
        }
    }

    private int getLimitForLeftSwipe() {
        return getPaddingLeft() - getDragDistance();
    }

    private void initSwipeActionHandling() {
        wasChecked = false;
        isNormalLeftSwipeTriggered = false;
        isLongSwipeTriggered = false;
        getElementWidth();
    }

    private void handleRightSwipeAction(int offset) {
        int limit = getLimitForLeftSwipe() + LONG_SWIPE_ACTIVATION_TOLERANCE;
        if (offset < 0 && offset <= limit) {
            isRightSwipeTriggered = true;
        } else {
            isRightSwipeTriggered = false;
        }
    }

    private void checkItemSelection() {
        if (isRightSwipeTriggered) {
            rightSwipeSelected();
        } else if (isNormalLeftSwipeTriggered) {
            normalSwipeElementSelected();
        } else if (isLongSwipeTriggered) {
            longSwipeElementSelected();
        }
    }

    private void rightSwipeSelected() {
        Timber.d("Normal right swipe item selected");
        if (itemSwipeListener != null) {
            itemSwipeListener.onRightSwipe();
        }
    }

    private void normalSwipeElementSelected() {
        Timber.d("Normal left swipe item selected");
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

    private void delayClose() {
        closeHandler.postDelayed(() -> close(true, true), AUTO_CLOSE_DELAY);
    }
}
