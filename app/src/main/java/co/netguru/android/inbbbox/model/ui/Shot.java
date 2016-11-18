package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.model.api.ShotEntity;

@AutoValue
public abstract class Shot implements Parcelable, ShotImage {

    public abstract long id();

    @Nullable
    public abstract String title();

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

    public abstract boolean isBucketed();

    public abstract boolean isLiked();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Shot.Builder id(long id);

        public abstract Shot.Builder title(String title);

        public abstract Shot.Builder team(Team team);

        public abstract Shot.Builder projectUrl(String url);

        public abstract Shot.Builder date(LocalDateTime date);

        public abstract Shot.Builder likesCount(Integer likeCount);

        public abstract Shot.Builder bucketCount(Integer bucketCount);

        public abstract Shot.Builder description(String description);

        public abstract Shot.Builder isLiked(boolean state);

        public abstract Shot.Builder isBucketed(boolean state);

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
                .author(User.create(shotEntity.getUser()))
                .title(shotEntity.getTitle())
                .description(shotEntity.getDescription())
                .team(shotEntity.getTeam() != null ? Team.create(shotEntity.getTeam()) : null)
                .bucketCount(shotEntity.getBucketsCount())
                .likesCount(shotEntity.getLikesCount())
                // TODO: 17.11.2016
                .isBucketed(false)
                .isLiked(false)
                .date(shotEntity.getCreatedAt())
                .isGif(shotEntity.getAnimated())
                .hdpiImageUrl(shotEntity.getImage().hiDpiUrl())
                .normalImageUrl(shotEntity.getImage().normalUrl())
                .thumbnailUrl(shotEntity.getImage().teaserUrl())
                .build();
    }

}
