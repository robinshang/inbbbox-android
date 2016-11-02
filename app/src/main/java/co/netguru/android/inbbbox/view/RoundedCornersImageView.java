package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedCornersImageView extends ImageView {

    public static float radius = 18.0f;
    private Path clipPath;
    private RectF rect;

    public RoundedCornersImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

    private void init() {
        //float radius = 36.0f;
        clipPath = new Path();
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
    }
}
