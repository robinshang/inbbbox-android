package co.netguru.android.inbbbox.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

@AutoValue
public abstract class FilteredShotsParams implements Serializable {

    @Nullable
    public abstract String list();

    @Nullable
    public abstract String timeFrame();

    //date format YYYY-MM-DD
    @Nullable
    public abstract String date();

    @Nullable
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
