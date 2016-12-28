package co.netguru.android.inbbbox.model.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.model.localrepository.database.FollowerEntityDB;

@AutoValue
public abstract class FollowerEntity {

    public abstract long id();

    @SerializedName("created_at")
    public abstract ZonedDateTime createdAt();

    @SerializedName("followee")
    public abstract UserEntity user();

    public static TypeAdapter<FollowerEntity> typeAdapter(Gson gson) {
        return new AutoValue_FollowerEntity.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_FollowerEntity.Builder();
    }

    public static FollowerEntity fromDB(FollowerEntityDB followerEntityDB) {
        return FollowerEntity.builder()
                .id(followerEntityDB.getId())
                .createdAt(followerEntityDB.getCreatedAt())
                .user(UserEntity.fromDB(followerEntityDB.getUser()))
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder createdAt(ZonedDateTime createdAt);

        public abstract Builder user(UserEntity user);

        public abstract FollowerEntity build();
    }
}
