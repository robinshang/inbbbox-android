package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import co.netguru.android.inbbbox.model.api.TeamEntity;
import co.netguru.android.inbbbox.model.localrepository.database.TeamDB;

@AutoValue
public abstract class Team implements Parcelable {

    public abstract long id();

    public abstract String name();

    public static Team.Builder builder() {
        return new AutoValue_Team.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Team.Builder id(long id);

        public abstract Team.Builder name(String name);

        public abstract Team build();
    }

    public static Team create(TeamEntity entity) {
        return Team.builder()
                .id(entity.id())
                .name(entity.name())
                .build();
    }

    public static Team fromDB(TeamDB teamDB) {
        return Team.builder()
                .id(teamDB.getId())
                .name(teamDB.getName())
                .build();
    }

    public static TypeAdapter<Team> typeAdapter(Gson gson) {
        return new AutoValue_Team.GsonTypeAdapter(gson);
    }
}
