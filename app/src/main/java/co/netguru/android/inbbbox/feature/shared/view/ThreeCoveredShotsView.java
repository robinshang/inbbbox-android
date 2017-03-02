package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public class ThreeCoveredShotsView extends FrameLayout {

    @BindView(R.id.three_covered_shots_first_view)
    RoundedCornersShotImageView firstShotImageView;
    @BindView(R.id.three_covered_shots_second_view)
    RoundedCornersShotImageView secondShotImageView;
    @BindView(R.id.three_covered_shots_third_view)
    RoundedCornersShotImageView thirdShotImageView;

    public ThreeCoveredShotsView(Context context) {
        super(context);
        init();
    }

    public ThreeCoveredShotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThreeCoveredShotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.triple_shots_view, this);
        ButterKnife.bind(this);
    }
}
