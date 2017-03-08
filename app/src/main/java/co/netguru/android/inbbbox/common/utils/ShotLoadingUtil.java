package co.netguru.android.inbbbox.common.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.graphics.CallbackAnimationDrawable;
import co.netguru.android.inbbbox.data.shot.model.ui.ShotImage;
import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;

public class ShotLoadingUtil {

    private final static String GIF_EXTENSION = ".gif";

    private static boolean isLoad = false;
    private static GlideDrawable glideResource;

    private ShotLoadingUtil() {
        throw new AssertionError();
    }

    private static String getImageUrl(ShotImage shot) {
        return (shot.hiDpiImageUrl() != null && !shot.hiDpiImageUrl().isEmpty())
                ? shot.hiDpiImageUrl() : shot.normalImageUrl();
    }

    private static DrawableTypeRequest<String> getThumbnailRequest(Context context, String url) {
        return Glide.with(context)
                .load(url);
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

    public static void loadMainViewShot(Context context, ImageView placeholder, ImageView target, ShotImage shot) {
        loadMainViewShotWithListener(context, placeholder, target, shot, getRequestListener(shot));
    }

    public static void loadMainViewShotWithListener(Context context, ImageView placeholder, ImageView target, ShotImage shot,
                                                    RequestListener<String, GlideDrawable> requestListener) {
        String imageUrl = getImageUrl(shot);
        Timber.d("shot image url: %s", imageUrl);

        final boolean isGif = isGif(imageUrl);
        if (isGif) {
            placeholder.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            placeholder.setBackgroundResource(R.drawable.basketball_loader);
            AnimationDrawable animationDrawable = (AnimationDrawable) placeholder.getBackground();
            animationDrawable.setCallback(stopAnimation(animationDrawable, target));

            animationDrawable.start();

            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(target);
            Glide.with(context)
                    .load(imageUrl)
                    .listener(requestListener)
                    .error(R.drawable.logo_empty)
                    .animate(android.R.anim.fade_in)
                    .into(imageViewTarget);

        } else {
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(target);
            Glide.with(context)
                    .load(imageUrl)
                    .listener(requestListener)
                    .placeholder(R.drawable.shot_placeholder)
                    .thumbnail(ShotLoadingUtil.getThumbnailRequest(context, shot.thumbnailUrl()))
                    .animate(android.R.anim.fade_in)
                    .into(imageViewTarget);
        }
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

    private static boolean isGif(@NonNull String url) {
        return url.endsWith(GIF_EXTENSION);
    }

    private static RequestListener<String, GlideDrawable> getRequestListener(ShotImage shotImage) {
        if (isGif(getImageUrl(shotImage))) {
            return new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    isLoad = true;
                    glideResource = resource;
                    return true;
                }
            };
        } else {
            return null;
        }
    }

    private static CallbackAnimationDrawable stopAnimation(final AnimationDrawable animationDrawable, ImageView target) {
        return new CallbackAnimationDrawable(animationDrawable, target) {
            @Override
            public void onAnimationComplete() {
                if (isLoad) {
                    animationDrawable.stop();
                    target.setImageDrawable(glideResource);
                    glideResource.start();
                    isLoad = false;
                    glideResource = null;
                } else {
                    animationDrawable.start();
                }

            }
        };
    }
}