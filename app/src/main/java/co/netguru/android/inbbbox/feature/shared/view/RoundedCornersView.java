package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public abstract class RoundedCornersView extends FrameLayout {

    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    @BindColor(R.color.windowBackground)
    int defaultRoundedCornersBackground;
    private boolean isRoundingBottomCornersEnabled;

    private Path clipPathTop = new Path();
    private Path clipPathBottom = new Path();
    private RectF clippingRectTop;
    private RectF clippingRectBottom;

    public RoundedCornersView(Context context) {
        super(context);
        init();
    }

    public RoundedCornersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        handleAttrs(context, attrs);
    }

    public RoundedCornersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        handleAttrs(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initClippingPath();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (clippingRectTop == null || clippingRectBottom == null) {
            initClippingPath();
        }
        canvas.clipPath(clipPathTop);
        if (isRoundingBottomCornersEnabled)
            canvas.clipPath(clipPathBottom, Region.Op.INTERSECT);
        super.draw(canvas);
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    private void handleAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornersView);
        isRoundingBottomCornersEnabled =
                a.getBoolean(R.styleable.RoundedCornersView_roundingBottomCornersEnabled,
                        true);
        a.recycle();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResource(), this);
        ButterKnife.bind(this);
        setWillNotDraw(false);
    }

    private void initClippingPath() {
        clipPathTop.reset();
        clippingRectTop = new RectF(0, 0, getWidth(), getHeight() + radius);
        clipPathTop.addRoundRect(clippingRectTop, radius, radius, Path.Direction.CW);

        clipPathBottom.reset();
        clippingRectBottom = new RectF(0, -radius, getWidth(), getHeight());
        clipPathBottom.addRoundRect(clippingRectBottom, radius, radius, Path.Direction.CW);
    }
}
