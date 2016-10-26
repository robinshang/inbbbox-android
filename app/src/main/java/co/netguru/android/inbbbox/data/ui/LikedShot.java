package co.netguru.android.inbbbox.data.ui;

import android.support.annotation.Nullable;

public class LikedShot implements ImageThumbnail {

    private final int id;
    private final String imageUrl;

    public LikedShot(int id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    @Override
    public String getThumbnailUrl() {
        return null;
    }

    @Nullable
    @Override
    public Integer getPlaceholderResId() {
        return null;
    }

    @Nullable
    @Override
    public Integer getErrorImageResId() {
        return null;
    }
}
