package co.netguru.android.inbbbox.model.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Project {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("name")
    public abstract String name();

    public static TypeAdapter<Project> typeAdapter(Gson gson) {
        return new AutoValue_Project.GsonTypeAdapter(gson);
    }

    public static Project create(long id, String name) {
        return new AutoValue_Project(id, name);
    }
}
