package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;

import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.model.api.ShotEntity;

@AutoValue
public abstract class Shot implements Parcelable, ShotImage {

    public abstract Integer id();

    @Nullable
    public abstract String title();

    public abstract LocalDateTime creationDate();

    @Nullable
    public abstract String projectUrl();

    public abstract LocalDateTime date();

    public abstract Integer likesCount();

    public abstract Integer bucketCount();

    @Nullable
    public abstract String description();

    @Override
    public abstract boolean isGif();

    @Override
    @Nullable
    public abstract String hdpiImageUrl();

    @Override
    @Nullable
    public abstract String normalImageUrl();

    @Nullable
    public abstract User author();

    @Nullable
    public abstract Team team();

    @Override
    @Nullable
    public abstract String thumbnailUrl();

    @Nullable
    public abstract Boolean isBucketed();

    @Nullable
    public abstract Boolean isLiked();

    public static Builder update(Shot shot) {
        return Shot.builder()
                .id(shot.id())
                .title(shot.title())
                .author(shot.author())
                .team(shot.team())
                .projectUrl(shot.projectUrl())
                .date(shot.date())
                .likesCount(shot.likesCount())
                .bucketCount(shot.bucketCount())
                .description(shot.description())
                .isGif(shot.isGif())
                .hdpiImageUrl(shot.hdpiImageUrl())
                .normalImageUrl(shot.normalImageUrl())
                .thumbnailUrl(shot.thumbnailUrl())
                .isLiked(shot.isLiked())
                .isBucketed(shot.isBucketed());
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Shot.Builder id(long id);

        public abstract Shot.Builder title(String title);

        public abstract Shot.Builder team(Team team);

        public abstract Shot.Builder projectUrl(String url);

        public abstract Shot.Builder date(LocalDateTime date);

        public abstract Shot.Builder likesCount(Integer likeCount);

        public abstract Shot.Builder bucketCount(Integer bucketCount);

        public abstract Shot.Builder creationDate(LocalDateTime localDateTime);

        public abstract Shot.Builder description(String description);

        public abstract Shot.Builder isLiked(Boolean state);

        public abstract Shot.Builder isBucketed(Boolean state);

        public abstract Shot.Builder isGif(boolean state);

        public abstract Shot.Builder hdpiImageUrl(String url);

        public abstract Shot.Builder normalImageUrl(String url);

        public abstract Shot.Builder thumbnailUrl(String url);

        public abstract Shot.Builder author(User user);

        public abstract Shot build();
    }

    public static Shot.Builder builder() {
        return new AutoValue_Shot.Builder();
    }

    public static Shot create(ShotEntity shotEntity) {
        return Shot.builder()
                .id(shotEntity.getId())
                .author(shotEntity.getUser() != null ? User.create(shotEntity.getUser()) : null)
                .title(shotEntity.getTitle())
                .creationDate(shotEntity.getCreatedAt())
                .description(shotEntity.getDescription())
                .team(shotEntity.getTeam() != null ? Team.create(shotEntity.getTeam()) : null)
                .bucketCount(shotEntity.getBucketsCount())
                .likesCount(shotEntity.getLikesCount())
                .date(shotEntity.getCreatedAt())
                .isGif(shotEntity.getAnimated())
                .isLiked(false)
                .isBucketed(false)
                .hdpiImageUrl(shotEntity.getImage().hiDpiUrl())
                .normalImageUrl(shotEntity.getImage().normalUrl())
                .thumbnailUrl(shotEntity.getImage().teaserUrl())
                .build();
    }
}
