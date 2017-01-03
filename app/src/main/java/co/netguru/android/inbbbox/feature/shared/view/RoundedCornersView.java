package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
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
    int backgroundColor;

    private Bitmap maskBitmap;
    private Paint maskPaint;

    public RoundedCornersView(Context context) {
        super(context);
        init();
    }

    public RoundedCornersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
        final Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mask);
        final Paint cornersMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        cornersMaskPaint.setColor(backgroundColor);
        canvas.drawRect(0, 0, width, height, cornersMaskPaint);
        cornersMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, cornersMaskPaint);

        return mask;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResource(), this);
        ButterKnife.bind(this);
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setWillNotDraw(false);
    }
}
