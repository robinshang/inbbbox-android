package co.netguru.android.inbbbox.feature.shared.view.swipingpanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shared.view.RoundedViewClipper;

public class LongSwipeLayout extends SwipeLayout {

    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    private boolean isRoundingBottomCornersEnabled;
    private boolean isRoundingEnabled;

    private static final long AUTO_CLOSE_DELAY = 300;

    private boolean isLeftSwipeTriggered;
    private boolean isLeftLongSwipeTriggered;
    private boolean isRightSwipeTriggered;
    private boolean isRightLongSwipeTriggered;
    private ItemSwipeListener itemSwipeListener;
    private int swipeLimitShift = 0;
    private RoundedViewClipper viewClipper;

    public LongSwipeLayout(Context context) {
        super(context);
        init();
    }

    public LongSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttrs(context, attrs);
        init();
    }

    public LongSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handleAttrs(context, attrs);
        init();
    }

    public void setSwipeLimitShift(int swipeLimitShift) {
        this.swipeLimitShift = swipeLimitShift;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initSwipeActionHandling();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            checkItemSelection();
            close(true);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            handleSwipingActions();
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
        ButterKnife.bind(this);
        setWillNotDraw(false);
        viewClipper = new RoundedViewClipper(radius, getWidth(), getHeight(),
                isRoundingBottomCornersEnabled);
        setLayerType(LAYER_TYPE_HARDWARE, viewClipper.getDrawingPaint());
    }

    private void handleSwipingActions() {
        int swipeLimit = getSwipeLimit();
        int positionX = getSurfaceView().getLeft();

        itemSwipeListener.onSwipeProgress(positionX, swipeLimit);

        if (swipeLimit > 0) {
            handleSwipeLeft(positionX, swipeLimit);
            handleSwipeRight(positionX, swipeLimit);
        }
    }

    private void handleSwipeLeft(int positionX, int swipeLimit) {
        if (positionX >= swipeLimit) {
            isLeftLongSwipeTriggered = true;
        }

        if (positionX >= swipeLimit / 2) {
            isLeftSwipeTriggered = true;
        }

        if (positionX < swipeLimit) {
            isLeftLongSwipeTriggered = false;
        }

        if (positionX < swipeLimit / 2) {
            isLeftSwipeTriggered = false;
        }
    }

    private void handleSwipeRight(int positionX, int swipeLimit) {
        if (positionX <= -swipeLimit) {
            isRightLongSwipeTriggered = true;
            isRightSwipeTriggered = false;
        } else if (positionX <= -swipeLimit / 2) {
            isRightLongSwipeTriggered = false;
            isRightSwipeTriggered = true;
        } else {
            isRightLongSwipeTriggered = false;
            isRightSwipeTriggered = false;
        }
    }

    private int getSwipeLimit() {
        return -(getPaddingLeft() - getDragDistance() + swipeLimitShift);
    }

    private void initSwipeActionHandling() {
        isLeftSwipeTriggered = false;
        isLeftLongSwipeTriggered = false;
    }

    private void checkItemSelection() {
        if (isRightSwipeTriggered) {
            rightSwipeSelected();
        } else if (isLeftSwipeTriggered) {
            leftSwipeSelected();
        }

        if (isLeftLongSwipeTriggered) {
            leftLongSwipeSelected();
        }
        if (isRightLongSwipeTriggered) {
            rightLongSwipeSelected();
        }
    }

    private void rightSwipeSelected() {
        if (itemSwipeListener != null) {
            itemSwipeListener.onRightSwipe();
        }
    }

    private void leftSwipeSelected() {
        if (itemSwipeListener != null) {
            itemSwipeListener.onLeftSwipe();
        }
    }

    private void leftLongSwipeSelected() {
        if (itemSwipeListener != null) {
            itemSwipeListener.onLeftLongSwipe();
        }
    }

    private void rightLongSwipeSelected() {
        if (itemSwipeListener != null) {
            itemSwipeListener.onRightLongSwipe();
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
                itemSwipeListener.onStartSwipe();
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
                //no-op
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                itemSwipeListener.onEndSwipe();
                delayClose();
            }
        };
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isRoundingEnabled)
            viewClipper.clip(canvas, getWidth(), getHeight());
    }

    private void handleAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornersView);
        isRoundingBottomCornersEnabled =
                a.getBoolean(R.styleable.RoundedCornersView_roundingBottomCornersEnabled, true);
        isRoundingEnabled = a.getBoolean(R.styleable.RoundedCornersView_roundingEnabled, true);
        a.recycle();
    }

    @Override
    protected void onDetachedFromWindow() {
        viewClipper.reset();
        super.onDetachedFromWindow();
    }
}
