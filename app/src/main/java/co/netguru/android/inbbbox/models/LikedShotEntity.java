package co.netguru.android.inbbbox.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class LikedShotEntity {

    public abstract long id();

    @SerializedName("created_at")
    public abstract String createdAt();

    public abstract ShotEntity shot();

    public static TypeAdapter<LikedShotEntity> typeAdapter(Gson gson) {
        return new AutoValue_LikedShotEntity.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_LikedShotEntity.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long value);

        public abstract Builder createdAt(String value);

        public abstract Builder shot(ShotEntity value);

        public abstract LikedShotEntity build();
    }
}
