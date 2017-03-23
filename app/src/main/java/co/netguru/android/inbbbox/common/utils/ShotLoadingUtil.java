package co.netguru.android.inbbbox.common.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.ShotImage;
import co.netguru.android.inbbbox.feature.shared.view.AnimationDrawableCallback;
import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;

public class ShotLoadingUtil {

    private ShotLoadingUtil() {
        throw new AssertionError();
    }

    public static void loadListShot(Context context, ImageView target, ShotImage shot) {
        target.setImageResource(R.drawable.shot_placeholder);
        Glide.clear(target);
        Glide.with(context)
                .load(shot.normalImageUrl())
                .thumbnail(ShotLoadingUtil.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(target);
    }

    public static void loadListBlurredShotWithListener(Context context, ImageView target, ShotImage shot,
                                                       RequestListener<String, GlideDrawable> requestListener) {
        target.setImageResource(R.drawable.shot_placeholder);
        Glide.clear(target);
        Glide.with(context)
                .load(shot.normalImageUrl())
                .thumbnail(ShotLoadingUtil.getThumbnailRequest(context, shot.thumbnailUrl()))
                .listener(requestListener)
                .bitmapTransform(new BlurTransformation(context))
                .into(target);
    }

    public static void loadMainViewShot(Context context, ImageView targetView, ShotImage shot) {
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(targetView);
        Glide.with(context)
                .load(getImageUrl(shot))
                .placeholder(R.drawable.shot_placeholder)
                .thumbnail(ShotLoadingUtil.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(imageViewTarget);
    }

    public static void loadMainViewShotWithPlaceholder(Context context, ImageView placeholder,
                                                       ImageView target, ShotImage shot) {
        loadMainViewShotWithPlaceholderAndListener(context, placeholder, target, shot, null);
    }

    public static void loadMainViewShotWithPlaceholderAndListener(Context context, ImageView placeholderView,
                                                                  ImageView targetView, ShotImage shot,
                                                                  RequestListener<String, GlideDrawable> requestListener) {
        Glide.clear(targetView);
        String imageUrl = getImageUrl(shot);

        Glide.clear(placeholderView);
        placeholderView.setVisibility(View.GONE);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(targetView);
        Glide.with(context)
                .load(imageUrl)
                .listener(requestListener)
                .placeholder(R.drawable.shot_placeholder)
                .thumbnail(ShotLoadingUtil.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(imageViewTarget);

    }

    public static void loadMainShotWithGifAnimation(Context context, ImageView placeholderView,
                                                    ImageView targetView, ShotImage shot,
                                                    AnimationDrawable animationDrawable) {
        placeholderView.setVisibility(View.VISIBLE);
        targetView.setVisibility(View.GONE);

        final String imageUrl = getImageUrl(shot);
        Glide.clear(placeholderView);
        Glide.clear(targetView);

        animationDrawable.start();

        Glide.with(context)
                .load(imageUrl)
                .listener(createRequestListener(placeholderView, targetView, animationDrawable))
                .error(R.drawable.logo_empty)
                .dontAnimate()
                .override(targetView.getMaxWidth(), targetView.getMaxHeight())
                .into(new GlideDrawableImageViewTarget(targetView));
    }

    public static void loadMainViewShotNoPlaceholder(Context context,
                                                     ImageView target,
                                                     ShotImage shot) {

        String imageUrl = getImageUrl(shot);
        Timber.d("shot image url: %s", imageUrl);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(target);
        Glide.with(context)
                .load(imageUrl)
                .animate(android.R.anim.fade_in)
                .into(imageViewTarget);
    }

    private static RequestListener<String, GlideDrawable> createRequestListener(ImageView placeholderView,
                                                                                ImageView targetView,
                                                                                AnimationDrawable animationDrawable) {
        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                       boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                           boolean isFromMemoryCache, boolean isFirstResource) {
                stopAnimationIfGifFromCacheOrNotifyGifReady(isFromMemoryCache, placeholderView, targetView, animationDrawable);
                return false;
            }
        };
    }

    private static void stopAnimationIfGifFromCacheOrNotifyGifReady(boolean isFromMemoryCache,
                                                                    ImageView placeholderView, ImageView targetView,
                                                                    AnimationDrawable animationDrawable) {
        if (!isFromMemoryCache) {
            final AnimationDrawableCallback animationCallback =
                    (AnimationDrawableCallback) animationDrawable.getCallback();
            if (animationCallback != null) {
                animationCallback.setShouldFinishAnimation(true);
            }
        } else {
            animationDrawable.stop();
            placeholderView.setVisibility(View.GONE);
            targetView.setVisibility(View.VISIBLE);
        }
    }

    private static String getImageUrl(ShotImage shot) {
        return (shot.hiDpiImageUrl() != null && !shot.hiDpiImageUrl().isEmpty())
                ? shot.hiDpiImageUrl() : shot.normalImageUrl();
    }

    private static DrawableTypeRequest<String> getThumbnailRequest(Context context, String url) {
        return Glide.with(context)
                .load(url);
    }
}