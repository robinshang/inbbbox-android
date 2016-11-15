package co.netguru.android.inbbbox.model.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

@AutoValue
public abstract class Bucket {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("name")
    public abstract String name();

    @Nullable
    @SerializedName("description")
    public abstract String description();

    @SerializedName("shots_count")
    public abstract int shotsCount();

    @SerializedName("created_at")
    public abstract LocalDateTime createdAt();

    public static TypeAdapter<Bucket> typeAdapter(Gson gson) {
        return new AutoValue_Bucket.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_Bucket.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder description(String description);

        public abstract Builder shotsCount(int shotsCount);

        public abstract Builder createdAt(LocalDateTime createdAt);

        public abstract Bucket build();
    }
}