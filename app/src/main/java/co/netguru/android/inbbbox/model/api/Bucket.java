package co.netguru.android.inbbbox.model.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;

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

    @Nullable
    @SerializedName("user")
    public abstract UserEntity user();

    @SerializedName("created_at")
    public abstract LocalDateTime createdAt();

    public static TypeAdapter<Bucket> typeAdapter(Gson gson) {
        return new AutoValue_Bucket.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_Bucket.Builder();
    }

    public static Bucket.Builder update(Bucket bucket) {
        return Bucket.builder()
                .createdAt(bucket.createdAt())
                .description(bucket.description())
                .id(bucket.id())
                .shotsCount(bucket.shotsCount())
                .name(bucket.name())
                .user(bucket.user());
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder description(String description);

        public abstract Builder shotsCount(int shotsCount);

        public abstract Builder createdAt(LocalDateTime createdAt);

        public abstract Builder user(UserEntity userEntity);

        public abstract Bucket build();
    }
}