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

    public abstract String userAvatarUrl();

    public abstract String authorName();

    public abstract String authorUrl();

    @Nullable
    public abstract String companyName();

    @Nullable
    public abstract String companyProfileUrl();

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

    @Override
    @Nullable
    public abstract String thumbnailUrl();

    public abstract Integer authorId();

    public abstract boolean isBucketed();

    public abstract boolean isLiked();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Shot.Builder id(long id);

        public abstract Shot.Builder title(String title);

        public abstract Shot.Builder userAvatarUrl(String userAvatarUrl);

        public abstract Shot.Builder authorName(String authorName);

        public abstract Shot.Builder authorUrl(String authorUrl);

        public abstract Shot.Builder teamName(String companyName);

        public abstract Shot.Builder teamProfileUrl(String companyProfileUrl);

        public abstract Shot.Builder projectUrl(String url);

        public abstract Shot.Builder date(LocalDateTime date);

        public abstract Shot.Builder likesCount(Integer likeCount);

        public abstract Shot.Builder bucketCount(Integer bucketCount);

        public abstract Shot.Builder description(String description);

        public abstract Shot.Builder authorId(Integer description);

        public abstract Shot.Builder isLiked(boolean state);

        public abstract Shot.Builder isBucketed(boolean state);

        public abstract Shot.Builder isGif(boolean state);

        public abstract Shot.Builder hdpiImageUrl(String url);

        public abstract Shot.Builder normalImageUrl(String url);

        public abstract Shot.Builder thumbnailUrl(String url);

        public abstract Shot build();
    }

    public static Shot.Builder builder() {
        return new AutoValue_Shot.Builder();
    }

    public static Shot create(ShotEntity shotEntity) {
        return Shot.builder()
                .id(shotEntity.getId())
                .authorId(shotEntity.getUser().id())
                .authorName(shotEntity.getUser().name())
                .title(shotEntity.getTitle())
                .description(shotEntity.getDescription())
                // TODO: 17.11.2016
                .projectUrl(shotEntity.getHtmlUrl())
                .teamName(shotEntity.getTeam() != null ? shotEntity.getTeam().getName() : null)
                .teamProfileUrl(shotEntity.getTeam() != null ? shotEntity.getTeam().getTeamShotsUrl() : null)
                .userAvatarUrl(shotEntity.getUser().avatarUrl())
                .bucketCount(shotEntity.getBucketsCount())
                .likesCount(shotEntity.getLikesCount())
                // TODO: 17.11.2016
                .isBucketed(false)
                .isLiked(false)
                .authorUrl(shotEntity.getUser().htmlUrl())
                .date(shotEntity.getCreatedAt())
                .isGif(shotEntity.getAnimated())
                .hdpiImageUrl(shotEntity.getImage().hiDpiUrl())
                .normalImageUrl(shotEntity.getImage().normalUrl())
                .thumbnailUrl(shotEntity.getImage().teaserUrl())
                .build();
    }

}
