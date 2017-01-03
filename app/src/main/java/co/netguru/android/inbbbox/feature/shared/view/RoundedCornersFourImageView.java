package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;

public class RoundedCornersFourImageView extends RoundedCornersView {

    @BindView(R.id.image_top_left)
    ImageView topLeftImage;
    @BindView(R.id.image_top_right)
    ImageView topRightImage;
    @BindView(R.id.image_bottom_left)
    ImageView bottomLeftImage;
    @BindView(R.id.image_bottom_right)
    ImageView bottomRightImage;

    public RoundedCornersFourImageView(Context context) {
        super(context);
    }

    public RoundedCornersFourImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCornersFourImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @LayoutRes
    @Override
    protected int getLayoutResource() {
        return R.layout.four_images_view_layout;
    }

    public ImageView getTopLeftImage() {
        return topLeftImage;
    }

    public ImageView getTopRightImage() {
        return topRightImage;
    }

    public ImageView getBottomLeftImage() {
        return bottomLeftImage;
    }

    public ImageView getBottomRightImage() {
        return bottomRightImage;
    }
}
