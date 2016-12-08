package co.netguru.android.inbbbox.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.ShotImage;
import timber.log.Timber;

public class ShotLoadingManager {

    private ShotLoadingManager() {
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
                .thumbnail(ShotLoadingManager.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(target);
    }

    public static void loadMainViewShot(Context context, ImageView target, ShotImage shot) {
        loadMainViewShotWithListener(context, target, shot, null);
    }

    public static void loadMainViewShotWithListener(Context context, ImageView target, ShotImage shot,
                                                    RequestListener requestListener) {
        String imageUrl = getImageUrl(shot);
        Timber.d("shot image url: %s", imageUrl);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(target);
        Glide.with(context)
                .load(imageUrl)
                .listener(requestListener)
                .placeholder(R.drawable.shot_placeholder)
                .thumbnail(ShotLoadingManager.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(imageViewTarget);
    }
}