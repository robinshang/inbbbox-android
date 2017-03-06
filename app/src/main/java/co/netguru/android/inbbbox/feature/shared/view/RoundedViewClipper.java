package co.netguru.android.inbbbox.feature.shared.view;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class RoundedViewClipper {

    private float radius;
    private int height;
    private int width;
    private boolean isRoundingBottomCornersEnabled;

    private Path clipPathTop = new Path();
    private Path clipPathBottom = new Path();

    private Paint visiblePaint;
    private Bitmap topMaskBitmap;
    private Bitmap bottomMaskBitmap;
    private Paint drawingPaint;
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    public RoundedViewClipper(float radius, int width, int height,
                              boolean isRoundingBottomCornersEnabled) {
        this.radius = radius;
        this.height = height;
        this.width = width;
        this.isRoundingBottomCornersEnabled = isRoundingBottomCornersEnabled;
    }

    /**
     * Call in {@link android.view.View} that you want to clip with its drawing {@link Canvas}
     * and current width and height
     *
     * @param canvas
     * @param width
     * @param height
     */
    public void clip(Canvas canvas, int width, int height) {
        if (!isReady())
            initMask(width, height);
        canvas.drawBitmap(topMaskBitmap, 0, 0, drawingPaint);
        if (isRoundingBottomCornersEnabled)
            canvas.drawBitmap(bottomMaskBitmap, 0, 0, drawingPaint);
    }

    /**
     * Call to return {@link Paint} used for drawing so hardware acceleration can be set in
     * {@link android.view.View}
     * @return paint used for drawing
     */
    public Paint getDrawingPaint() {
        return drawingPaint;
    }

    /**
     * Call to init mask in case of size changes with current width and height,
     * Don't call in {@link com.daimajia.swipe.SwipeLayout}
     * as it calls onSizeChanged in a way irrelevant to this use
     * case
     * @param width
     * @param height
     */
    public void initMask(int width, int height) {
        this.height = height;
        this.width = width;

        reset();
        initPaints();
        initClippingPath();

        topMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas topMaskCanvas = new Canvas(topMaskBitmap);
        visiblePaint.setColor(Color.BLACK);
        topMaskCanvas.drawPath(clipPathTop, visiblePaint);

        bottomMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas bottomMaskCanvas = new Canvas(bottomMaskBitmap);
        visiblePaint.setColor(Color.BLACK);
        bottomMaskCanvas.drawPath(clipPathBottom, visiblePaint);
    }

    /**
     * Call when detaching from window to free resources
     */
    public void reset() {
        if (drawingPaint != null)
            drawingPaint.setXfermode(null);

        if (topMaskBitmap != null) {
            topMaskBitmap.recycle();
        }

        if (bottomMaskBitmap != null) {
            bottomMaskBitmap.recycle();
        }
    }

    private void initPaints() {
        drawingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawingPaint.setXfermode(porterDuffXfermode);
        visiblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        visiblePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void initClippingPath() {
        clipPathTop.reset();
        RectF clippingRectTop = new RectF(0, 0, width, height + radius);
        clipPathTop.addRoundRect(clippingRectTop, radius, radius, Path.Direction.CW);

        clipPathBottom.reset();
        RectF clippingRectBottom = new RectF(0, -radius, width, height);
        clipPathBottom.addRoundRect(clippingRectBottom, radius, radius, Path.Direction.CW);
    }

    private boolean isReady() {
        return !(topMaskBitmap == null ||
                bottomMaskBitmap == null ||
                topMaskBitmap.isRecycled() ||
                bottomMaskBitmap.isRecycled());
    }
}
