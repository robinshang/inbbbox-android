package co.netguru.android.inbbbox.feature.likes.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.LikedShot;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static co.netguru.android.inbbbox.utils.PixelConverterUtil.convertToPx;


public class LikesViewHolder extends BaseViewHolder<LikedShot> {

    // TODO: 31.10.2016 move this constants to resources
    private static final int RADIUS_DP = 2;
    private static final int RADIUS_MARGIN = 0;

    @BindView(R.id.like_item_image_view)
    ImageView imageView;

    LikesViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LikedShot item) {
        Context context = itemView.getContext();
        Glide.with(itemView.getContext())
                .load(item.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context,
                        convertToPx(RADIUS_DP, context),
                        RADIUS_MARGIN))
                .placeholder(R.drawable.ic_likes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}