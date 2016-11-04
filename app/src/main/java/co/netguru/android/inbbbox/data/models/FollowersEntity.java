package co.netguru.android.inbbbox.data.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class FollowersEntity {

    public abstract long id();

    @SerializedName("created_at")
    public abstract String createdAt();

    @SerializedName("followee")
    public abstract User user();

    public static TypeAdapter<FollowersEntity> typeAdapter(Gson gson) {
        return new AutoValue_FollowersEntity.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_FollowersEntity.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder user(User user);

        public abstract FollowersEntity build();
    }
}
