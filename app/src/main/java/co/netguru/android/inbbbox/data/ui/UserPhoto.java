package co.netguru.android.inbbbox.data.ui;

import android.support.annotation.Nullable;

import co.netguru.android.inbbbox.R;

public class UserPhoto implements ImageThumbnail {

    private final String userImageUrl;

    public UserPhoto(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    @Override
    public String getImageUrl() {
        return userImageUrl;
    }

    @Nullable
    @Override
    public String getThumbnailUrl() {
        return null;
    }

    @Override
    public Integer getPlaceholderResId() {
        return R.drawable.ic_ball_active;
    }

    @Override
    public Integer getErrorImageResId() {
        return R.drawable.ic_ball_active;
    }
}
