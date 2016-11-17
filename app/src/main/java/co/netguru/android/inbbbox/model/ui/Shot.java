package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;

import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.model.api.ShotEntity;

@AutoValue
public abstract class Shot implements Parcelable, ShotImage {

    public abstract long id();

    @Nullable
    public abstract String title();

    public abstract LocalDateTime creationDate();

    @Nullable
    public abstract String description();

    @Nullable
    public abstract String hdpiImageUrl();

    @Nullable
    public abstract String normalImageUrl();

    @Nullable
    public abstract String thumbnailUrl();

    @Nullable
    public abstract String authorAvatarUrl();

    @Nullable
    public abstract String authorName();

    @Nullable
    public abstract Team team();

    public abstract boolean isLiked();

    public abstract boolean isGif();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Shot.Builder id(long id);

        public abstract Shot.Builder title(String title);

        public abstract Shot.Builder creationDate(LocalDateTime localDateTime);

        public abstract Shot.Builder description(String description);

        public abstract Shot.Builder hdpiImageUrl(String hdpiImageUrl);

        public abstract Shot.Builder normalImageUrl(String normalImageUrl);

        public abstract Shot.Builder thumbnailUrl(String thumbnailUrl);

        public abstract Shot.Builder isLiked(boolean isLiked);

        public abstract Shot.Builder authorAvatarUrl(String url);

        public abstract Shot.Builder authorName(String name);

        public abstract Shot.Builder team(Team team);

        public abstract Shot.Builder isGif(boolean isGif);

        public abstract Shot build();
    }

    public static Shot.Builder builder() {
        return new AutoValue_Shot.Builder();
    }

    public static Shot create(ShotEntity shotEntity) {
        Shot.Builder builder = Shot.builder()
                .id(shotEntity.getId())
                .title(shotEntity.getTitle())
                .creationDate(shotEntity.getCreatedAt())
                .description(shotEntity.getDescription())
                .hdpiImageUrl(shotEntity.getImage().hiDpiUrl())
                .normalImageUrl(shotEntity.getImage().normalUrl())
                .thumbnailUrl(shotEntity.getImage().teaserUrl())
                .isLiked(false)
                .team(shotEntity.getTeam() == null ? null : Team.create(shotEntity.getTeam().getId(), shotEntity.getTeam().getName()))
                .isGif(shotEntity.getAnimated());
        if (shotEntity.getUser() != null) {
            builder.authorAvatarUrl(shotEntity.getUser().avatarUrl())
                    .authorName(shotEntity.getUser().name());
        }
        return builder.build();
    }

    @AutoValue
    public abstract static class Team implements Parcelable {

        public abstract long id();

        public abstract String name();

        public static Team create(long id, String name) {
            return new AutoValue_Shot_Team(id, name);
        }
    }
}
