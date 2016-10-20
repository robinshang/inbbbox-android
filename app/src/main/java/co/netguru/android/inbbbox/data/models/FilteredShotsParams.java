package co.netguru.android.inbbbox.data.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

import co.netguru.android.inbbbox.utils.Constants.API;

import static android.R.attr.type;

@AutoValue
public abstract class FilteredShotsParams implements Serializable {

    @SerializedName(API.SHOTS_KEY_LIST)
    public abstract String list();

    @SerializedName(API.SHOTS_KEY_TIME_FRAME)
    public abstract String timeFrame();

    //    date format YYYY-MM-DD
    @SerializedName(API.SHOTS_KEY_DATE)
    public abstract String date();

    @SerializedName(API.SHOTS_KEY_SORT)
    public abstract String sort();

    public static TypeAdapter<FilteredShotsParams> typeAdapter(Gson gson) {
        return new AutoValue_FilteredShotsParams.GsonTypeAdapter(gson);
    }

    public static Builder newBuilder() {
        return new AutoValue_FilteredShotsParams.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder list(String value);

        public abstract Builder timeFrame(String value);

        public abstract Builder date(String value);

        public abstract Builder sort(String string);

        public abstract FilteredShotsParams build();
    }
}
