package co.netguru.android.inbbbox.view.swipingpanel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.daimajia.swipe.SwipeLayout;

import timber.log.Timber;

public class LongSwipeLayout extends SwipeLayout {

    private static final long AUTO_CLOSE_DELAY = 300;
    private static final int LONG_SWIPE_ACTIVATION_TOLERANCE = 80;
    private static final int RIGHT_SWIPE_ACTIVATION_TOLERANCE = 80;

    private boolean wasChecked = false;
    private boolean wasFirstElementWidthCollected = false;
    private boolean isLongSwipeTriggered;
    private boolean isRightSwipeTriggered;
    private boolean isNormalSwipeTriggered;
    private ItemSwipeListener itemSwipeListener;

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
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initSwipeActionHandling();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Timber.d("Action UP");
            checkItemSelection();
            close(true);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            handleSwipingActions(event);

        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            Timber.d("Action Cancel");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void processHandRelease(float xvel, float yvel, boolean isCloseBeforeDragged) {
        //keep it false to automatically close the surface
        super.processHandRelease(xvel, yvel, false);
    }

    private void init() {
        setShowMode(ShowMode.LayDown);
        addSwipeListener(getSwipeListener());
    }

    private void getElementWidth() {
        if (!wasFirstElementWidthCollected) {
            wasFirstElementWidthCollected = true;
        }
    }

    private void handleSwipingActions(MotionEvent event) {
        int swipingLimit = getLimitForLeftSwipe();

        if (-getSurfaceView().getLeft() < swipingLimit / 3 && !wasChecked && !isNormalSwipeTriggered) {
            wasChecked = true;
            isNormalSwipeTriggered = true;
            isLongSwipeTriggered = false;
        }

        if (-getSurfaceView().getLeft() > swipingLimit / 3) {
            isNormalSwipeTriggered = false;
        }

        if (-getSurfaceView().getLeft() < swipingLimit + LONG_SWIPE_ACTIVATION_TOLERANCE) {
            isLongSwipeTriggered = true;
            wasChecked = false;
        } else {
            isLongSwipeTriggered = false;
        }

        if (-getSurfaceView().getLeft() > swipingLimit / 3) {
            isNormalSwipeTriggered = false;
            wasChecked = false;
        }

        itemSwipeListener.onLeftSwipeActivate(isNormalSwipeTriggered);
        itemSwipeListener.onLeftLongSwipeActivate(isLongSwipeTriggered);
        itemSwipeListener.onRightSwipeActivate(isRightSwipeTriggered);
    }

    private int getLimitForLeftSwipe() {
        return getPaddingLeft() - getDragDistance();
    }

    @Override
    protected void dispatchSwipeEvent(int surfaceLeft, int surfaceTop, boolean open) {
        Timber.d("open: " + open);
        super.dispatchSwipeEvent(surfaceLeft, surfaceTop, open);
    }

    private void initSwipeActionHandling() {
        wasChecked = false;
        isNormalSwipeTriggered = false;
        isLongSwipeTriggered = false;
        getElementWidth();
    }

    private void handleRightSwipeAction(int offset) {
        int limit = getLimitForLeftSwipe() + RIGHT_SWIPE_ACTIVATION_TOLERANCE;
        if (offset < 0 && offset <= limit) {
            isRightSwipeTriggered = true;
        } else {
            isRightSwipeTriggered = false;
        }
    }

    private void checkItemSelection() {
        if (isRightSwipeTriggered) {
            rightSwipeSelected();
        } else if (isNormalSwipeTriggered) {
            normalSwipeElementSelected();
        }
        if (isLongSwipeTriggered) {
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
        postDelayed(() -> close(true, true), AUTO_CLOSE_DELAY);
    }

    public void setItemSwipeListener(ItemSwipeListener itemSwipeListener) {

        this.itemSwipeListener = itemSwipeListener;
    }

    public SwipeListener getSwipeListener() {
        return new SwipeListener() {
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
    }
}
