package co.netguru.android.inbbbox.data.ui;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Shot {
    @Nullable
    public abstract Integer id();

    @Nullable
    public abstract String title();

    @Nullable
    public abstract String description();

    @Nullable
    public abstract String hdpiImageUrl();

    @Nullable
    public abstract String normalImageUrl();

    @Nullable
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
}
