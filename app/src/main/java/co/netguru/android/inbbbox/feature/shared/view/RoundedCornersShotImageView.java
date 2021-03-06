package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.ShotLoadingUtil;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class RoundedCornersShotImageView extends RoundedCornersView {

    @BindView(R.id.shot_image_view)
    AspectRatioImageView shotImageView;
    @BindView(R.id.gif_label_textView)
    TextView gifLabel;

    public RoundedCornersShotImageView(Context context) {
        super(context);
    }

    public RoundedCornersShotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCornersShotImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @LayoutRes
    @Override
    protected int getLayoutResource() {
        return R.layout.rounded_corners_shot_image_view;
    }

    public ImageView getImageView() {
        return shotImageView;
    }

    public void loadShot(Shot shot) {
        ShotLoadingUtil.loadListShot(getContext(), shotImageView, shot);
        gifLabel.setVisibility(shot.isGif() ? VISIBLE : GONE);
    }

    public void loadBlurredShotWithListener(Shot shot, RequestListener<String, GlideDrawable> listener) {
        ShotLoadingUtil.loadListBlurredShotWithListener(getContext(), shotImageView, shot, listener);
        gifLabel.setVisibility(shot.isGif() ? VISIBLE : GONE);
    }

    public void loadPlaceHolder(Drawable drawable) {
        shotImageView.setImageDrawable(drawable);
    }
}
