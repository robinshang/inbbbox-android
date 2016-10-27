package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;

import butterknife.ButterKnife;
import timber.log.Timber;

public class LongSwipeLayout extends SwipeLayout {

    private static final float LONG_SWIPE_TRIGGERING_THRESHOLD = 300;
    private View surfaceView;

    private int initialPadding;
    private float offsetX;
    private boolean wasChecked = false;
    private boolean wasTriggered;
    private float triggeredXOfsset;

    public LongSwipeLayout(Context context) {
        super(context);
    }

    public LongSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    protected void dispatchSwipeEvent(int surfaceLeft, int surfaceTop, int dx, int dy) {
//        int max = 640;
//
//        if (surfaceLeft > (640 / 2) - 80 && surfaceLeft < (640 / 2)) {
//            Timber.d("TRIGGERED--------->");
//        } else {
//            Timber.d("leftOffset: " + surfaceLeft);
//        }
//        super.dispatchSwipeEvent(2, surfaceTop, 2, dy);
//        Timber.d("Dispatching dx:" + dx + ", dy: " + dy+" --sF: "+surfaceLeft);
//
//    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
        surfaceView = getSurfaceView();
        initialPadding = surfaceView.getLeft();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            wasChecked = false;
            wasTriggered = false;
        }

        int difference = initialPadding - surfaceView.getLeft();
        if (difference > -252) {
            Timber.d("Dispatching: " + difference + " -evX: " + event.getX());
        }
        if (difference < -252 && !wasChecked) {
            offsetX = event.getX();
            wasChecked = true;
            wasTriggered = true;
        }
        if (event.getX() - offsetX > LONG_SWIPE_TRIGGERING_THRESHOLD) {
            wasTriggered = false;
        }
        if (wasTriggered) {
            Timber.d("DISPATCH TRIGGERED!!! --------------------offset: " + offsetX + " -------real: " + event.getX());
            event.setLocation(offsetX, event.getY());
        } else if (wasChecked) {

            float offset = event.getX() - LONG_SWIPE_TRIGGERING_THRESHOLD;
            event.setLocation(offset, event.getY());
            Timber.d("dispatch offseet: " + offset + " -evX: " + event.getX());
        }

        return super.onTouchEvent(event);
    }
}
