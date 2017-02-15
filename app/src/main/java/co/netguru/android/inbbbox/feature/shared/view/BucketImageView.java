package co.netguru.android.inbbbox.feature.shared.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindViews;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public class BucketImageView extends FrameLayout {

    public static final int IMAGES = 4;
    public static final float BIG_IMAGE_HEIGHT_FACTOR = 0.75f;
    public static final float SMALL_IMAGE_WIDTH_FACTOR = 1f / (IMAGES - 1);

    public static final int ANIMATE_EVERY_MILLISECONDS_MIN = 3000;
    public static final int ANIMATE_EVERY_MILLISECONDS_MAX = 6000;
    public static final int ANIMATION_LENGTH = 1000;

    @BindDimen(R.dimen.shot_corner_radius)
    float radius;

    @BindColor(R.color.windowBackground)
    int backgroundColor;

    @BindViews({
            R.id.bucket_four_images_image1,
            R.id.bucket_four_images_image2,
            R.id.bucket_four_images_image3,
            R.id.bucket_four_images_image4})
    List<ImageView> imageViews;

    int currentImage;
    private Bitmap maskBitmap;
    private Paint maskPaint;
    private Timer timer;

    public BucketImageView(Context context) {
        super(context);
        initView();
    }

    public BucketImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BucketImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ImageView getImageView(int position) {
        return imageViews.get(position);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (maskBitmap == null) {
            maskBitmap = createMask(canvas.getWidth(), canvas.getHeight());
        }

        canvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
    }

    public void startAnimation() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        initGrid();
        scheduleTimer();
    }

    public void onPause() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public void onResume() {
        if (timer != null) {
            scheduleTimer();
        }
    }

    private void scheduleTimer() {
        int delay = ANIMATE_EVERY_MILLISECONDS_MIN +
                new Random().nextInt(ANIMATE_EVERY_MILLISECONDS_MAX - ANIMATE_EVERY_MILLISECONDS_MIN);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                post(() -> {
                    currentImage = (currentImage + 1) % IMAGES;

                    animateImageMatrixFromFirstSmallToBig(imageViews.get(currentImage), ANIMATION_LENGTH);
                    animateImageMatrixToMoveTowardsFirst(imageViews.get((currentImage + 1) % IMAGES), 1, 0, ANIMATION_LENGTH);
                    animateImageMatrixToMoveTowardsFirst(imageViews.get((currentImage + 2) % IMAGES), 2, 1, ANIMATION_LENGTH);
                    animateImageMatrixFromBigtoLastSmall(imageViews.get((currentImage + 3) % IMAGES), ANIMATION_LENGTH);
                });
            }
        }, delay, delay);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.bucket_four_images_view, this, true);
        ButterKnife.bind(this);
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setWillNotDraw(false);
    }

    private void initGrid() {
        post(() -> {
            for (ImageView imageView : imageViews) {
                imageView.setVisibility(View.VISIBLE);
            }

            animateImageMatrixFromFirstSmallToBig(imageViews.get(currentImage), 0);
            animateImageMatrixToMoveTowardsFirst(imageViews.get((currentImage + 1) % IMAGES), 1, 0, 0);
            animateImageMatrixToMoveTowardsFirst(imageViews.get((currentImage + 2) % IMAGES), 2, 1, 0);
            animateImageMatrixFromBigtoLastSmall(imageViews.get((currentImage + 3) % IMAGES), 0);
        });
    }

    private void animateImageMatrixFromFirstSmallToBig(ImageView imageView, int duration) {
        imageView.bringToFront();
        Matrix matrix = imageView.getMatrix();

        float drawableWidth = imageView.getDrawable().getIntrinsicWidth();
        float drawableHeight = imageView.getDrawable().getIntrinsicHeight();
        float scaleFactor = smallImageWidth() / drawableWidth;
        float fromY = imageView.getHeight() - (scaleFactor * drawableHeight);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float value = (Float) animation.getAnimatedValue();

            float scaleValue = SMALL_IMAGE_WIDTH_FACTOR + value * (1 - SMALL_IMAGE_WIDTH_FACTOR);
            float translationY = fromY - value * fromY;
            matrix.setScale(scaleValue, scaleValue);
            matrix.postTranslate(0, translationY);

            imageView.setImageMatrix(matrix);
        });
        animator.start();
    }

    private void animateImageMatrixFromBigtoLastSmall(ImageView imageView, int duration) {
        final Matrix matrix = imageView.getMatrix();
        final Drawable drawable = imageView.getDrawable();
        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();
        float scaleFactor = smallImageWidth() / drawableWidth;
        float viewHeight = imageView.getHeight();
        float fromX = 0;
        // animate to last position (there are only IMAGES-1 images in lower row,
        // so the index of the last one is IMAGES-2)
        float toX = (IMAGES - 2) * smallImageWidth();
        float fromY = 0;
        float toY = viewHeight - (scaleFactor * drawableHeight);

        float fromScale = 1;
        float toScale = SMALL_IMAGE_WIDTH_FACTOR;

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float value = (Float) animation.getAnimatedValue();

            float scale = toScale + (fromScale - toScale) * (1 - value);
            float translationX = fromX + (toX - fromX) * value;
            float translationY = fromY + (toY - fromY) * value;
            matrix.setScale(scale, scale);
            matrix.postTranslate(translationX, translationY);

            imageView.setImageMatrix(matrix);
        });
        animator.start();
    }

    private void animateImageMatrixToMoveTowardsFirst(ImageView imageView, int fromPosition,
                                                      int toPosition, int duration) {
        final Matrix matrix = imageView.getMatrix();
        final Drawable drawable = imageView.getDrawable();
        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();
        float scaleFactor = smallImageWidth() / drawableWidth;
        float viewHeight = imageView.getHeight();
        float fromX = fromPosition * smallImageWidth();
        float toX = toPosition * smallImageWidth();

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float value = (Float) animation.getAnimatedValue();

            float translationX = toX + (fromX - toX) * (1 - value);
            float translationY = viewHeight - (scaleFactor * drawableHeight);
            matrix.setScale(SMALL_IMAGE_WIDTH_FACTOR, SMALL_IMAGE_WIDTH_FACTOR);
            matrix.postTranslate(translationX, translationY);

            imageView.setImageMatrix(matrix);
        });
        animator.start();
    }

    private float smallImageWidth() {
        return getWidth() * SMALL_IMAGE_WIDTH_FACTOR;
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
}
