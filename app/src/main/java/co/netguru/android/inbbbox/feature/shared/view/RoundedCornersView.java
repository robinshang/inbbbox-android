package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public abstract class RoundedCornersView extends FrameLayout {

    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    private boolean isRoundingBottomCornersEnabled;
    private boolean isRoundingEnabled;
    private RoundedViewClipper viewClipper;

    public RoundedCornersView(Context context) {
        super(context);
        init();
    }

    public RoundedCornersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttrs(context, attrs);
        init();
    }

    public RoundedCornersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttrs(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            viewClipper.initMask(w, h);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isRoundingEnabled)
            viewClipper.clip(canvas, getWidth(), getHeight());
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    private void handleAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornersView);
        isRoundingBottomCornersEnabled =
                a.getBoolean(R.styleable.RoundedCornersView_roundingBottomCornersEnabled, true);
        isRoundingEnabled = a.getBoolean(R.styleable.RoundedCornersView_roundingEnabled, true);
        a.recycle();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResource(), this);
        ButterKnife.bind(this);
        setWillNotDraw(false);
        viewClipper = new RoundedViewClipper(radius, getWidth(), getHeight(),
                isRoundingBottomCornersEnabled);
        setLayerType(LAYER_TYPE_HARDWARE, viewClipper.getDrawingPaint());
    }

    @Override
    protected void onDetachedFromWindow() {
        viewClipper.reset();
        super.onDetachedFromWindow();
    }
}
