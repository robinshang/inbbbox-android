package co.netguru.android.inbbbox.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public class RoundedCornersBucketFourImageView extends FrameLayout {

    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    @BindColor(R.color.windowBackground)
    int backgroundColor;

    @BindView(R.id.top_image_view)
    ImageView topImageView;

    @BindView(R.id.bottom_image_layout)
    PercentRelativeLayout bottomImageLayout;

    @BindView(R.id.bottom_first_image_view)
    ImageView bottomFirstImage;

    @BindView(R.id.bottom_second_image_view)
    ImageView bottomSecondImage;

    @BindView(R.id.bottom_third_image_view)
    ImageView bottomThirdImage;


    private Bitmap maskBitmap;
    private Paint maskPaint;

    public RoundedCornersBucketFourImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornersBucketFourImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornersBucketFourImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ImageView getTopImageView() {
        return topImageView;
    }

    public ImageView getBottomFirstImage() {
        return bottomFirstImage;
    }

    public ImageView getBottomSecondImage() {
        return bottomSecondImage;
    }

    public ImageView getBottomThirdImage() {
        return bottomThirdImage;
    }

    public void showBottomImages(boolean shouldBeShown) {
        bottomImageLayout.setVisibility(shouldBeShown ? View.VISIBLE : View.GONE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (maskBitmap == null) {
            maskBitmap = createMask(canvas.getWidth(), canvas.getHeight());
        }

        canvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
    }

    private Bitmap createMask(int width, int height) {
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);

        Paint cornersMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cornersMaskPaint.setColor(backgroundColor);

        canvas.drawRect(0, 0, width, height, cornersMaskPaint);

        cornersMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, cornersMaskPaint);

        return mask;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bucket_four_images_view, this, true);
        ButterKnife.bind(this);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setWillNotDraw(false);
    }

}
