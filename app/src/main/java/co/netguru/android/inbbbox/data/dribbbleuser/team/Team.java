package co.netguru.android.inbbbox.data.dribbbleuser.team;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import co.netguru.android.inbbbox.data.db.TeamDB;

@AutoValue
public abstract class Team implements Parcelable {

    public abstract long id();

    public abstract String name();

    public abstract String username();

    public abstract String avatarUrl();

    public abstract int shotsCount();

    public static Team.Builder builder() {
        return new AutoValue_Team.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Team.Builder id(long id);

        public abstract Team.Builder name(String name);

        public abstract Team.Builder username(String username);

        public abstract Team.Builder avatarUrl(String avatarUrl);

        public abstract Team.Builder shotsCount(int shotsCount);

        public abstract Team build();
    }

    public static Team create(TeamEntity entity) {
        return Team.builder()
                .id(entity.id())
                .name(entity.name())
                .username(entity.username())
                .avatarUrl(entity.avatarUrl())
                .shotsCount(entity.shotsCount())
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
