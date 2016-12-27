package co.netguru.android.inbbbox.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import co.netguru.android.inbbbox.R;

public class RoundedCornersImageView extends ImageView {

    private final float defaultRadius = getContext().getResources().getDimension(R.dimen.shot_corner_radius);
    private float radius = defaultRadius;
    private boolean isBottomEdgeRoundingDisabled = false;
    private Path clipPath;

    public RoundedCornersImageView(Context context) {
        super(context);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        clipPath = new Path();
        handleAttrs(context, attrs);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handleAttrs(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();

        clipPath.reset();
        RectF rect = new RectF(0, 0, width, height);
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        if (isBottomEdgeRoundingDisabled) {
            RectF rectMask = new RectF(0, height / 2, width, height);
            clipPath.addRoundRect(rectMask, 0, 0, Path.Direction.CW);
        }

        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

    public void setRadius(float radius) {

        this.radius = radius;
    }

    public void disableRadiusForBottomEdge(boolean disable) {
        this.isBottomEdgeRoundingDisabled = disable;
    }

    private void handleAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornersImageView);
        radius = a.getDimension(R.styleable.AspectRatioImageView_aspectRatio, defaultRadius);
        a.recycle();
    }
}
