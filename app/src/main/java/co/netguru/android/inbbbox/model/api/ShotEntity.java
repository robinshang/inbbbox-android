package co.netguru.android.inbbbox.model.api;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

@AutoValue
public abstract class ShotEntity implements Parcelable {

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
    public abstract int likesCount();

    @SerializedName("buckets_count")
    public abstract int bucketsCount();

    @SerializedName("created_at")
    public abstract ZonedDateTime createdAt();

    @SerializedName("comments_count")
    public abstract int commentsCount();

    @SerializedName("animated")
    public abstract boolean animated();

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

        public abstract Builder likesCount(int likesCount);

        public abstract Builder bucketsCount(int bucketsCount);

        public abstract Builder commentsCount(int commentsCount);

        public abstract Builder createdAt(ZonedDateTime createdAt);

        public abstract Builder animated(boolean animated);

        public abstract Builder user(UserEntity user);

        public abstract Builder team(TeamEntity team);

        public abstract ShotEntity build();
    }
}