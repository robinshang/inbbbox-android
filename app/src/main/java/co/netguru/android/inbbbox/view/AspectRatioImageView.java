package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import co.netguru.android.inbbbox.R;

/**
 * Maintains an aspect ratio based on either width or height.
 */
public class AspectRatioImageView extends ImageView {
    public static final int MEASUREMENT_WIDTH = 0;
    public static final int MEASUREMENT_HEIGHT = 1;

    private static final float DEFAULT_UNDEFINED_ASPECT_RATIO = -1;
    private static final int DEFAULT_DOMINANT_MEASUREMENT = MEASUREMENT_WIDTH;

    private float aspectRatio;
    private int dominantMeasurement;

    public AspectRatioImageView(Context context) {
        this(context, null);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_UNDEFINED_ASPECT_RATIO);
        dominantMeasurement = a.getInt(R.styleable.AspectRatioImageView_dominantMeasurement,
                DEFAULT_DOMINANT_MEASUREMENT);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (Float.compare(aspectRatio, DEFAULT_UNDEFINED_ASPECT_RATIO) > 0) {
            int newWidth;
            int newHeight;
            switch (dominantMeasurement) {
                case MEASUREMENT_WIDTH:
                    newWidth = getMeasuredWidth();
                    newHeight = (int) (newWidth / aspectRatio);
                    break;

                case MEASUREMENT_HEIGHT:
                    newHeight = getMeasuredHeight();
                    newWidth = (int) (newHeight / aspectRatio);
                    break;

                default:
                    throw new IllegalStateException("Unknown measurement with ID " + dominantMeasurement);
            }

            setMeasuredDimension(newWidth, newHeight);
        }
    }
}