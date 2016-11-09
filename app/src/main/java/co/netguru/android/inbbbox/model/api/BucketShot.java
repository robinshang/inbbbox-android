package co.netguru.android.inbbbox.model.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

@AutoValue
public abstract class BucketShot {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("description")
    public abstract String description();

    @SerializedName("images")
    public abstract Image image();

    @SerializedName("created_at")
    public abstract LocalDateTime createdAt();

    @SerializedName("updated_at")
    public abstract LocalDateTime updatedAt();

    public static TypeAdapter<BucketShot> typeAdapter(Gson gson) {
        return new AutoValue_BucketShot.GsonTypeAdapter(gson);
    }
}
