package co.netguru.android.inbbbox.utils.imageloader;

import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.netguru.android.inbbbox.data.ui.ImageThumbnail;
import co.netguru.android.inbbbox.utils.Constants;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public final class GlideImageLoaderManager implements ImageLoader {

    private static final int RADIUS_DP = 2;
    private static final int RADIUS_MARGIN = 0;

    private Context context;

    public GlideImageLoaderManager(Context context) {

        this.context = context;
    }

    @Override
    public void loadImageWithThumbnail(ImageView destinationView, ImageThumbnail imageThumbnail) {
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(context)
                .load(imageThumbnail.getImageUrl());

        loadImage(destinationView, thumbnailRequest, imageThumbnail);
    }

    @Override
    public void loadImageWithRoundedCorners(ImageView destinationView,
                                            DrawableRequestBuilder<String> thumbnailRequest,
                                            ImageThumbnail imageThumbnail) {
        final DrawableRequestBuilder requestBuilder = createDrawableRequest(thumbnailRequest, imageThumbnail);
        requestBuilder.bitmapTransform(new RoundedCornersTransformation(context, convertToPx(RADIUS_DP), RADIUS_MARGIN));
        requestBuilder.into(destinationView);
    }

    @Override
    public void loadImage(ImageView destinationView,
                          DrawableRequestBuilder<String> thumbnailRequest,
                          ImageThumbnail imageThumbnail) {

        final DrawableRequestBuilder requestBuilder = createDrawableRequest(thumbnailRequest, imageThumbnail);
        requestBuilder.into(destinationView);
    }

    private DrawableRequestBuilder createDrawableRequest(DrawableRequestBuilder<String> thumbnailRequest,
                                                         ImageThumbnail imageThumbnail) {
        DrawableRequestBuilder requestBuilder = Glide.with(context)
                .load(imageThumbnail.getImageUrl());

        if (imageThumbnail.getPlaceholderResId() != null
                && imageThumbnail.getPlaceholderResId() != Constants.UNDEFINED) {
            requestBuilder.placeholder(imageThumbnail.getPlaceholderResId());
        }

        if (thumbnailRequest != null) {
            requestBuilder.thumbnail(thumbnailRequest);
        }

        if (imageThumbnail.getErrorImageResId() != null
                && imageThumbnail.getErrorImageResId() != Constants.UNDEFINED) {
            requestBuilder.error(imageThumbnail.getErrorImageResId());
        }
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestBuilder.animate(android.R.anim.fade_in);

        return requestBuilder;
    }

    private int convertToPx(int number) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, number,
                context.getResources().getDisplayMetrics());
    }
}
