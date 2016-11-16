package co.netguru.android.inbbbox.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.ShotImage;

public class ShotLoadingManager {

    private ShotLoadingManager() {
        throw new AssertionError();
    }

    private static String getImageUrl(ShotImage shot) {
        return (shot.hdpiImageUrl() != null && !shot.hdpiImageUrl().isEmpty())
                ? shot.hdpiImageUrl() : shot.normalImageUrl();
    }

    private static DrawableTypeRequest<String> getThumbnailRequest(Context context, String url) {
        return Glide.with(context)
                .load(url);
    }

    public static void loadListShot(Context context, ImageView target, ShotImage shot) {
        target.setImageResource(R.drawable.shot_placeholder);

        Glide.with(context)
                .load(shot.normalImageUrl())
                .thumbnail(ShotLoadingManager.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(target);
    }

    public static void loadMainViewShot(Context context, ImageView target, ShotImage shot) {
        target.setImageResource(R.drawable.shot_placeholder);

        DrawableTypeRequest<String> typeRequest = Glide.with(context)
                .load(getImageUrl(shot));

        if (shot.isGif()) {
            typeRequest.asGif();
        }
        typeRequest
                .thumbnail(ShotLoadingManager.getThumbnailRequest(context, shot.thumbnailUrl()))
                .animate(android.R.anim.fade_in)
                .into(target);
    }

}