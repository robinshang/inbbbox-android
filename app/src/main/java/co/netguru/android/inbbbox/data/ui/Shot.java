package co.netguru.android.inbbbox.data.ui;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import co.netguru.android.inbbbox.R;

@AutoValue
public abstract class Shot implements ImageThumbnail {
    public abstract Integer id();

    public abstract String title();

    public abstract String description();

    public abstract String hdpiImageUrl();

    public abstract String normalImageUrl();

    public abstract String thumbnailUrl();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Shot.Builder id(Integer id);

        public abstract Shot.Builder title(String title);

        public abstract Shot.Builder description(String description);

        public abstract Shot.Builder hdpiImageUrl(String hdpiImageUrl);

        public abstract Shot.Builder normalImageUrl(String normalImageUrl);

        public abstract Shot.Builder thumbnailUrl(String thumbnailUrl);

        public abstract Shot build();
    }

    public static Shot.Builder builder() {
        return new AutoValue_Shot.Builder();
    }

    public static TypeAdapter<Shot> typeAdapter(Gson gson) {
        return new AutoValue_Shot.GsonTypeAdapter(gson);
    }

    @Override
    public String getImageUrl() {
        return normalImageUrl();
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl();
    }

    @Override
    public Integer getPlaceholderResId() {
        return null;
    }

    @Override
    public Integer getErrorImageResId() {
        return null;
    }

    @Override
    public Integer getRoundCornersRadiusResId() {
        return R.dimen.shot_corner_radius;
    }
}
