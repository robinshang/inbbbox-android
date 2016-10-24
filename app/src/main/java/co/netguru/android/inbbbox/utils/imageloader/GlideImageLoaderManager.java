package co.netguru.android.inbbbox.utils.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.netguru.android.inbbbox.data.ui.ImageThumbnail;
import co.netguru.android.inbbbox.utils.Constants;

public class GlideImageLoaderManager implements ImageLoader {

    private Context context;

    public GlideImageLoaderManager(Context context) {

        this.context = context;
    }

    public void loadImageWithThumbnail(ImageView destinationView, ImageThumbnail imageThumbnail) {
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(context)
                .load(imageThumbnail.getImageUrl());

        loadImage(destinationView, thumbnailRequest, imageThumbnail);
    }

    public void loadImage(ImageView destinationView,
                          DrawableRequestBuilder<String> thumbnailRequest,
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
        requestBuilder.into(destinationView);
    }
}
