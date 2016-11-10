package co.netguru.android.inbbbox.model.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class FollowerEntity {

    public abstract long id();

    @SerializedName("created_at")
    public abstract String createdAt();

    @SerializedName("followee")
    public abstract User user();

    public static TypeAdapter<FollowerEntity> typeAdapter(Gson gson) {
        return new AutoValue_FollowerEntity.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_FollowerEntity.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder user(User user);

        public abstract FollowerEntity build();
    }
}
