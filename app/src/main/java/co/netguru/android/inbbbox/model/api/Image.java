package co.netguru.android.inbbbox.model.api;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import co.netguru.android.inbbbox.model.ui.ShotImage;


@AutoValue
public abstract class Image implements ShotImage, Parcelable {

    @Nullable
    @SerializedName("hidpi")
    public abstract String hiDpiImageUrl();

    @Nullable
    @SerializedName("normal")
    public abstract String normalImageUrl();

    @Nullable
    @SerializedName("teaser")
    public abstract String thumbnailUrl();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder hiDpiImageUrl(String hidpi);

        public abstract Builder normalImageUrl(String normal);

        public abstract Builder thumbnailUrl(String teaser);

        public abstract Image build();
    }

    public static Builder builder() {
        return new AutoValue_Image.Builder();
    }

    public static TypeAdapter<Image> typeAdapter(Gson gson) {
        return new AutoValue_Image.GsonTypeAdapter(gson);
    }
}
