package co.netguru.android.inbbbox.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import co.netguru.android.inbbbox.data.ui.ImageThumbnail;

public class ImageLoaderManager {


    private Context context;

    public ImageLoaderManager(Context context) {

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
        requestBuilder.into(destinationView);
    }
}
