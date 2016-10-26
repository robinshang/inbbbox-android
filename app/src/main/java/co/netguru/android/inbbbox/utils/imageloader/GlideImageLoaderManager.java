package co.netguru.android.inbbbox.utils.imageloader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.ui.ImageThumbnail;
import co.netguru.android.inbbbox.utils.Constants;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public final class GlideImageLoaderManager implements ImageLoader {

    private Context context;

    @Nullable
    private List<Transformation> transformations;

    private RoundedCornersTransformation roundedCornersTransformation;

    public GlideImageLoaderManager(Context context) {
        transformations = new ArrayList<>();
        this.context = context;
    }

    public void enableRoundCornersTransformationForNextRequest(float radius) {
        if (roundedCornersTransformation == null) {
            roundedCornersTransformation = new RoundedCornersTransformation(context,
                    Math.round(radius), 2);
        }
        transformations.add(roundedCornersTransformation);
    }

    public void disableRoundCornerTransformation() {
        roundedCornersTransformation = null;
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
            requestBuilder.bitmapTransform(new BlurTransformation(destinationView.getContext(), 25));
        }

        if (imageThumbnail.getErrorImageResId() != null
                && imageThumbnail.getErrorImageResId() != Constants.UNDEFINED) {
            requestBuilder.error(imageThumbnail.getErrorImageResId());
        }

        if (!transformations.isEmpty()) {
            Transformation[] transformationsArray = transformations
                    .toArray(new Transformation[transformations.size()]);

            requestBuilder.bitmapTransform(transformationsArray);
        }
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestBuilder.animate(android.R.anim.fade_in);
        requestBuilder.into(destinationView);
        transformations.clear();
    }
}
