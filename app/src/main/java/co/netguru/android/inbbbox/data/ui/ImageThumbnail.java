package co.netguru.android.inbbbox.data.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

public interface ImageThumbnail {

    String getImageUrl();

    @Nullable
    String getThumbnailUrl();

    @Nullable
    @DrawableRes
    Integer getPlaceholderResId();

    @Nullable
    @DrawableRes
    Integer getErrorImageResId();

    //Leave it null to disable rounded corners
    @Nullable
    Integer getRoundCornersRadius();
}
