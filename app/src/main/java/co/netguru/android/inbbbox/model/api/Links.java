package co.netguru.android.inbbbox.model.api;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Links implements Parcelable {

    @Nullable
    @SerializedName("web")
    public abstract String web();

    @Nullable
    @SerializedName("twitter")
    public abstract String twitter();

    public static Links create(String web, String twitter) {
        return new AutoValue_Links(web, twitter);
    }
    public static TypeAdapter<Links> typeAdapter(Gson gson) {
        return new AutoValue_Links.GsonTypeAdapter(gson);
    }
}
