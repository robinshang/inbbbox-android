package co.netguru.android.inbbbox.data.dribbbleuser.team;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import co.netguru.android.inbbbox.data.db.TeamDB;
import co.netguru.android.inbbbox.data.dribbbleuser.Links;

@AutoValue
public abstract class Team implements Parcelable {

    public abstract long id();

    public abstract String name();

    public abstract String username();

    public abstract String avatarUrl();

    public abstract int shotsCount();

    public abstract int followersCount();

    public abstract int followingsCount();

    public abstract int bucketsCount();

    public abstract int projectsCount();

    public abstract String bio();

    public abstract String location();

    public abstract Links links();

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

        public abstract Team.Builder followersCount(int followersCount);

        public abstract Team.Builder followingsCount(int followingsCount);

        public abstract Team.Builder bucketsCount(int bucketsCount);

        public abstract Team.Builder projectsCount(int projectsCount);

        public abstract Team.Builder bio(String bio);

        public abstract Team.Builder location(String location);

        public abstract Team.Builder links(Links links);

        public abstract Team build();
    }

    public static Team create(TeamEntity entity) {
        return Team.builder()
                .id(entity.id())
                .name(entity.name())
                .username(entity.username())
                .avatarUrl(entity.avatarUrl())
                .shotsCount(entity.shotsCount())
                .followersCount(entity.followersCount())
                .followingsCount(entity.followingsCount())
                .bucketsCount(entity.bucketsCount())
                .projectsCount(entity.projectsCount())
                .bio(entity.bio())
                .location(entity.location() == null ? "Unknown" : entity.location())
                .links(entity.links())
                .build();
    }

    public static Team fromDB(TeamDB teamDB) {
        return Team.builder()
                .id(teamDB.getId())
                .name(teamDB.getName())
                .username(teamDB.getUsername())
                .avatarUrl(teamDB.getAvatarUrl())
                .shotsCount(teamDB.getShotsCount())
                .followersCount(teamDB.getFollowersCount())
                .followingsCount(teamDB.getFollowingsCount())
                .bucketsCount(teamDB.getBucketsCount())
                .projectsCount(teamDB.getProjectsCount())
                .bio(teamDB.getBio())
                .location(teamDB.getLocation())
                .links(Links.create(teamDB.getLinks().getWeb(), teamDB.getLinks().getTwitter()))
                .build();
    }

    public static TypeAdapter<Team> typeAdapter(Gson gson) {
        return new AutoValue_Team.GsonTypeAdapter(gson);
    }
}
