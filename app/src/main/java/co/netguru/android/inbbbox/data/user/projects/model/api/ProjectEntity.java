package co.netguru.android.inbbbox.data.user.projects.model.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

@AutoValue
public abstract class ProjectEntity {

    public abstract long id();

    public abstract String name();

    @Nullable
    public abstract String description();

    @SerializedName("shots_count")
    public abstract int shotsCount();

    @SerializedName("created_at")
    public abstract ZonedDateTime createdAt();

    @SerializedName("updated_at")
    public abstract ZonedDateTime updatedAt();

    public static TypeAdapter<ProjectEntity> typeAdapter(Gson gson) {
        return new AutoValue_ProjectEntity.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_ProjectEntity.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder description(String description);

        public abstract Builder shotsCount(int shotsCount);

        public abstract Builder createdAt(ZonedDateTime createdAt);

        public abstract Builder updatedAt(ZonedDateTime updatedAt);

        public abstract ProjectEntity build();
    }
}
