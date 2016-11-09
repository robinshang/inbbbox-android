package co.netguru.android.inbbbox.model.api;

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

    @SerializedName("description")
    public abstract String description();

    @SerializedName("shots_count")
    public abstract int shotsCount();

    @SerializedName("created_at")
    public abstract LocalDateTime createdAt();

    @SerializedName("updated_at")
    public abstract LocalDateTime updatedAt();

    public static TypeAdapter<Bucket> typeAdapter(Gson gson) {
        return new AutoValue_Bucket.GsonTypeAdapter(gson);
    }
}