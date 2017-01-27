package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    @ColorInt
    private int roundedCornersBackgroundColor;
    private boolean isRoundingBottomCornersEnabled;
    private Bitmap maskBitmap;
    private Paint maskPaint;
    private Paint cornersMaskPaint;

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
            maskBitmap = createMask(w, h);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    private Bitmap createMask(int width, int height) {
        final Bitmap mask = Bitmap.createBitmap(width == 0 ? 1 : width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mask);

        mask.eraseColor(Color.TRANSPARENT);
        canvas.drawColor(roundedCornersBackgroundColor);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, cornersMaskPaint);

        if (!isRoundingBottomCornersEnabled) {
            canvas.drawRect(0, height - radius, width, height, cornersMaskPaint);
        }

        return mask;
    }

    private void handleAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornersView);
        roundedCornersBackgroundColor = a.getColor(R.styleable.RoundedCornersView_roundedCornersBackgroundColor,
                defaultRoundedCornersBackground);
        isRoundingBottomCornersEnabled = a.getBoolean(R.styleable.RoundedCornersView_roundingBottomCornersEnabled,
                true);
        a.recycle();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResource(), this);
        ButterKnife.bind(this);
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cornersMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cornersMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);
    }
}
