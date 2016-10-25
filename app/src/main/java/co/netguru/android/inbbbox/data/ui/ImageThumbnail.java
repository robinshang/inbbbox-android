package co.netguru.android.inbbbox.data.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

public interface ImageThumbnail {

    String getImageUrl();

    @Nullable
    String getThumbnailUrl();

    @DrawableRes
    Integer getPlaceholderResId();

    @DrawableRes
    Integer getErrorImageResId();
}
