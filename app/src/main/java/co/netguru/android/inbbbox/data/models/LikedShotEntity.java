package co.netguru.android.inbbbox.data.models;

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
        return null;
    }
}
