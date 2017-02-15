package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
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

    private Path clipPath = new Path();
    private RectF clippingRect;

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
    public void draw(Canvas canvas) {
        if (clippingRect == null) {
            initClippingPath();
        }
        if (isRoundingBottomCornersEnabled)
            canvas.clipPath(clipPath);
        super.draw(canvas);
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    private void handleAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornersView);
        isRoundingBottomCornersEnabled = a.getBoolean(R.styleable.RoundedCornersView_roundingBottomCornersEnabled,
                true);
        a.recycle();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResource(), this);
        ButterKnife.bind(this);
        setWillNotDraw(false);
    }

    private void initClippingPath() {
        clippingRect = new RectF(0, 0, getWidth(), getHeight());
        clipPath.addRoundRect(clippingRect, radius, radius, Path.Direction.CW);
    }
}
