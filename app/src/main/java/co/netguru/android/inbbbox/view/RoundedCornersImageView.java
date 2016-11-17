package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedCornersImageView extends ImageView {

    //default value
    private float radius = 0f;
    private boolean isBottomEdgeRoundingDisbled = false;

    public RoundedCornersImageView(Context context) {
        super(context);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();

        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, width, height);
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        if (isBottomEdgeRoundingDisbled) {
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
        this.isBottomEdgeRoundingDisbled = disable;
    }
}
