package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import co.netguru.android.inbbbox.R;

public class RoundedCornersFourImagesView extends PercentRelativeLayout {

    private static final int TOP_LEFT_VIEW_INDEX = 0;
    private static final int TOP_RIGHT_VIEW_INDEX = 1;
    private static final int BOTTOM_LEFT_VIEW_INDEX = 2;
    private static final int BOTTOM_RIGHT_VIEW_INDEX = 3;

    private Path clipPath;
    private RectF rect;
    private float radius;

    public RoundedCornersFourImagesView(Context context) {
        super(context);
        init(context);
    }

    public RoundedCornersFourImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundedCornersFourImagesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect.set(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public ImageView getTopLeftImageView() {
        return (ImageView) getChildAt(TOP_LEFT_VIEW_INDEX);
    }

    public ImageView getTopRightImageView() {
        return (ImageView) getChildAt(TOP_RIGHT_VIEW_INDEX);
    }

    public ImageView getBottomLeftImageView() {
        return (ImageView) getChildAt(BOTTOM_LEFT_VIEW_INDEX);
    }

    public ImageView getBottomRightImageView() {
        return (ImageView) getChildAt(BOTTOM_RIGHT_VIEW_INDEX);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.four_images_view_layout, this);
        setWillNotDraw(false);
        clipPath = new Path();
        rect = new RectF();
    }
}
