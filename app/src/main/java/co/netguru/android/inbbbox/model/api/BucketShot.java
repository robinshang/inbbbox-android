package co.netguru.android.inbbbox.model.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

@AutoValue
public abstract class BucketShot {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("description")
    public abstract String description();

    @SerializedName("images")
    public abstract Image image();

    @SerializedName("created_at")
    public abstract LocalDateTime createdAt();

    @SerializedName("updated_at")
    public abstract LocalDateTime updatedAt();

    public static TypeAdapter<BucketShot> typeAdapter(Gson gson) {
        return new AutoValue_BucketShot.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_BucketShot.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder title(String title);

        public abstract Builder description(String description);

        public abstract Builder image(Image image);

        public abstract Builder createdAt(LocalDateTime createdAt);

        public abstract Builder updatedAt(LocalDateTime updatedAt);

        public abstract BucketShot build();
    }
}
