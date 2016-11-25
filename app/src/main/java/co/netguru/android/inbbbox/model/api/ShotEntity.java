package co.netguru.android.inbbbox.model.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

@AutoValue
public abstract class ShotEntity {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("description")
    public abstract String description();

    @SerializedName("images")
    public abstract Image image();

    @SerializedName("likes_count")
    public abstract Integer likesCount();

    @SerializedName("buckets_count")
    public abstract Integer bucketsCount();

    @SerializedName("created_at")
    public abstract LocalDateTime createdAt();

    @SerializedName("buckets_url")
    public abstract String bucketsUrl();

    @SerializedName("likes_url")
    public abstract String likesUrl();

    @SerializedName("animated")
    public abstract Boolean animated();

    @Nullable
    @SerializedName("user")
    public abstract UserEntity user();

    @Nullable
    @SerializedName("team")
    public abstract TeamEntity team();

    public static Builder builder() {
        return new AutoValue_ShotEntity.Builder();
    }

    public static TypeAdapter<ShotEntity> typeAdapter(Gson gson) {
        return new AutoValue_ShotEntity.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder title(String title);

        public abstract Builder description(String description);

        public abstract Builder image(Image image);

        public abstract Builder likesCount(Integer likesCount);

        public abstract Builder bucketsCount(Integer bucketsCount);

        public abstract Builder createdAt(LocalDateTime createdAt);

        public abstract Builder bucketsUrl(String bucketsUrl);

        public abstract Builder likesUrl(String likesUrl);

        public abstract Builder animated(Boolean animated);

        public abstract Builder user(UserEntity user);

        public abstract Builder team(TeamEntity team);

        public abstract ShotEntity build();
    }
}