package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public class MultipleScrollingBackgroundsView extends FrameLayout {

    @BindView(R.id.scrolling_background_1)
    ScrollingBackgroundView scrollingBackgroundViewOne;

    @BindView(R.id.scrolling_background_2)
    ScrollingBackgroundView scrollingBackgroundViewTwo;

    @BindView(R.id.scrolling_background_3)
    ScrollingBackgroundView scrollingBackgroundViewThree;

    @BindView(R.id.scrolling_background_4)
    ScrollingBackgroundView scrollingBackgroundViewFour;


    public MultipleScrollingBackgroundsView(Context context) {
        this(context, null);
    }

    public MultipleScrollingBackgroundsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultipleScrollingBackgroundsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void startAnimation() {
        scrollingBackgroundViewOne.startAnimation();
        scrollingBackgroundViewTwo.startAnimation();
        scrollingBackgroundViewThree.startAnimation();
        scrollingBackgroundViewFour.startAnimation();
    }

    private void initView() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.view_multiple_scrolling_backgrounds, this, true);
        ButterKnife.bind(this);
    }
}
