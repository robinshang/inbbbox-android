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
        initPaints();
    }

    public void clip(Canvas canvas, int width, int height) {
        if (!isReady())
            initMask(width, height);
        drawingPaint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(topMaskBitmap, 0, 0, drawingPaint);
        if (isRoundingBottomCornersEnabled)
            canvas.drawBitmap(bottomMaskBitmap, 0, 0, drawingPaint);
        drawingPaint.setXfermode(null);
    }

    public Paint getDrawingPaint() {
        return drawingPaint;
    }

    private void initPaints() {
        drawingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        visiblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        visiblePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        visiblePaint.setAntiAlias(true);
    }

    public void initMask(int width, int height) {
        this.height = height;
        this.width = width;

        reset();
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

    private void initClippingPath() {
        clipPathTop.reset();
        RectF clippingRectTop = new RectF(0, 0, width, height + radius);
        clipPathTop.addRoundRect(clippingRectTop, radius, radius, Path.Direction.CW);

        clipPathBottom.reset();
        RectF clippingRectBottom = new RectF(0, -radius, width, height);
        clipPathBottom.addRoundRect(clippingRectBottom, radius, radius, Path.Direction.CW);
    }

    public void reset() {
        if (topMaskBitmap != null) {
            topMaskBitmap.recycle();
        }

        if (bottomMaskBitmap != null) {
            bottomMaskBitmap.recycle();
        }
    }

    private boolean isReady() {
        return !(topMaskBitmap == null ||
                bottomMaskBitmap == null ||
                topMaskBitmap.isRecycled() ||
                bottomMaskBitmap.isRecycled());
    }
}
