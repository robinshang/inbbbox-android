package co.netguru.android.inbbbox.feature.shared.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public class ScrollingBackgroundView extends FrameLayout {

    private static final long ANIMATION_DURATION = 80000L;
    private static final float ANIMATION_START_RATIO = 0.0f;
    private static final float ANIMATION_END_RATIO = 1.0f;
    private static final int DEFAULT_OFFSET = 0; // no offset
    private static final int DEFAULT_DIRECTION = 0; // no direction

    @BindView(R.id.background_image_one)
    ImageView backgroundViewOne;

    @BindView(R.id.background_image_two)
    ImageView backgroundViewTwo;

    @Nullable
    private Drawable drawable;
    private int scrollDirection = 0;
    private int startOffset = 0;

    public ScrollingBackgroundView(Context context) {
        this(context, null);
    }

    public ScrollingBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ScrollingBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public void startAnimation() {
        final ValueAnimator animator =
                ValueAnimator.ofFloat(ANIMATION_START_RATIO, ANIMATION_END_RATIO);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(innerAnimator -> {
            final float progress = (float) innerAnimator.getAnimatedValue();
            final float offset = backgroundViewOne.getHeight() * scrollDirection;
            final float translationY = offset * progress + startOffset;
            backgroundViewOne.setTranslationY(translationY);
            backgroundViewTwo.setTranslationY(translationY - offset);
        });
        animator.start();
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_scrolling_background, this, true);
        ButterKnife.bind(this);
        readAttributes(context, attrs);
        if (drawable != null) {
            backgroundViewOne.setImageDrawable(drawable);
            backgroundViewTwo.setImageDrawable(drawable);
        }
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollingBackgroundView);
        drawable = a.getDrawable(R.styleable.ScrollingBackgroundView_imageDrawable);
        scrollDirection = a.getInt(R.styleable.ScrollingBackgroundView_scrollDirection,
                DEFAULT_DIRECTION);
        startOffset = a.getInt(R.styleable.ScrollingBackgroundView_offset,
                DEFAULT_OFFSET);
        a.recycle();
    }
}
