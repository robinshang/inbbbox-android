package co.netguru.android.inbbbox.utils;

import android.content.Context;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

public class ThumbnailHelper {

    private ThumbnailHelper() {
        throw new AssertionError();
    }

    public static DrawableTypeRequest<String> getThumbnailRequest(Context context, String url) {
        return Glide.with(context)
                .load(url);
    }

}