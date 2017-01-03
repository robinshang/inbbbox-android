package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;

public class RoundedCornersBucketFourImageView extends RoundedCornersView {

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

    public RoundedCornersBucketFourImageView(Context context) {
        super(context);
    }

    public RoundedCornersBucketFourImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCornersBucketFourImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @LayoutRes
    @Override
    protected int getLayoutResource() {
        return R.layout.bucket_four_images_view;
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
}
