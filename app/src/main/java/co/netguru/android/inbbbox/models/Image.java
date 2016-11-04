package co.netguru.android.inbbbox.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@AutoValue
public abstract class Image implements Serializable {

    @Nullable
    @SerializedName("hidpi")
    public abstract String hiDpiUrl();

    @Nullable
    @SerializedName("normal")
    public abstract String normalUrl();

    @Nullable
    @SerializedName("teaser")
    public abstract String teaserUrl();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder hiDpiUrl(String hidpi);

        public abstract Builder normalUrl(String normal);

        public abstract Builder teaserUrl(String teaser);

        public abstract Image build();
    }

    public static Builder builder() {
        return new AutoValue_Image.Builder();
    }

    public static TypeAdapter<Image> typeAdapter(Gson gson) {
        return new AutoValue_Image.GsonTypeAdapter(gson);
    }
}
