package co.netguru.android.inbbbox.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.ShotImage;

public class ShotLoadingManager {

    private ShotLoadingManager() {
        throw new AssertionError();
    }

    static DrawableTypeRequest<String> getThumbnailRequest(Context context, String url) {
        return Glide.with(context)
                .load(url);

    }

    public static void loadShotImageView(Context context, ImageView target, ShotImage shot) {
        target.setImageResource(R.drawable.shot_placeholder);

        DrawableTypeRequest<String> typeRequest = getDrawableTypeRequest(context,
                getImageUrl(shot),
                shot.isGif());

        typeRequest
                .thumbnail(ShotLoadingManager.getThumbnailRequest(context, shot.thumbnailUrl()));
        executeRequest(typeRequest, target);
    }

    private static DrawableTypeRequest<String> getDrawableTypeRequest(Context context, String url, boolean isGif) {
        DrawableTypeRequest<String> typeRequest = Glide.with(context)
                .load(url);
        if (isGif) {
            typeRequest.asGif();
        } else {
            typeRequest.asBitmap();
        }
        return typeRequest;
    }

    public static String getImageUrl(ShotImage shot) {
        return (shot.hdpiImageUrl() != null && !shot.hdpiImageUrl().isEmpty())
                ? shot.hdpiImageUrl() : shot.normalImageUrl();
    }


    private static void executeRequest(DrawableTypeRequest<String> typeRequest, ImageView target) {
        typeRequest.diskCacheStrategy(DiskCacheStrategy.RESULT)
                .animate(android.R.anim.fade_in)
                .into(target);
    }
}