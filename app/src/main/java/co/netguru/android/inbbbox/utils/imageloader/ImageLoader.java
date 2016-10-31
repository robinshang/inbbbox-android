package co.netguru.android.inbbbox.utils.imageloader;

import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;

import co.netguru.android.inbbbox.data.ui.ImageThumbnail;

public interface ImageLoader {

    void loadImageWithThumbnail(ImageView destinationView, ImageThumbnail imageThumbnail);

    void loadImage(ImageView destinationView,
                   DrawableRequestBuilder<String> thumbnailRequest,
                   ImageThumbnail imageThumbnail);

    void loadImageWithRoundedCorners(ImageView destinationView,
                                     DrawableRequestBuilder<String> thumbnailRequest,
                                     ImageThumbnail imageThumbnail);
}
