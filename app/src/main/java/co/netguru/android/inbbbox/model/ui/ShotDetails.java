package co.netguru.android.inbbbox.model.ui;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import co.netguru.android.inbbbox.model.api.ShotEntity;


@AutoValue
public abstract class ShotDetails implements ShotImage {
    public abstract long id();

    public abstract String title();

    public abstract String userAvatarUrl();

    public abstract String authorName();

    public abstract String authorUrl();

    public abstract String companyName();

    public abstract String companyProfileUrl();

    public abstract String appName();

    public abstract LocalDateTime date();

    public abstract Integer likesCount();

    public abstract Integer bucketCount();

    public abstract String description();

    public abstract boolean isGif();

    public abstract String hdpiImageUrl();

    public abstract String normalImageUrl();

    public abstract String thumbnailUrl();

    @Nullable
    public abstract List<Comment> comments();

    public abstract Integer authorId();

    public abstract boolean isBucketed();

    public abstract boolean isLiked();

    public static ShotDetails createDetails(ShotEntity shotEntity, List<Comment> comments) {
        return ShotDetails.builder()
                .id(shotEntity.getId())
                .authorId(shotEntity.getUser().id())
                .authorName(shotEntity.getUser().name())
                .title(shotEntity.getTitle())
                .description(shotEntity.getDescription())
                // TODO: 17.11.2016
                .appName("???")
                .comments(comments)
                .companyName(shotEntity.getTeam().getName())
                .userAvatarUrl(shotEntity.getTeam().getAvatarUrl())
                .bucketCount(shotEntity.getBucketsCount())
                .likesCount(shotEntity.getLikesCount())
                // TODO: 17.11.2016
                .isBucketed(false)
                .isLiked(false)
                .companyProfileUrl(shotEntity.getTeam().getTeamShotsUrl())
                .authorUrl(shotEntity.getUser().htmlUrl())
                .date(shotEntity.getCreatedAt())
                .isGif(shotEntity.getAnimated())
                .hdpiImageUrl(shotEntity.getImage().hiDpiUrl())
                .normalImageUrl(shotEntity.getImage().normalUrl())
                .thumbnailUrl(shotEntity.getImage().teaserUrl())
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract ShotDetails.Builder id(long id);

        public abstract ShotDetails.Builder title(String title);

        public abstract ShotDetails.Builder comments(List<Comment> comments);

        public abstract ShotDetails.Builder userAvatarUrl(String userAvatarUrl);

        public abstract ShotDetails.Builder authorName(String authorName);

        public abstract ShotDetails.Builder authorUrl(String authorUrl);

        public abstract ShotDetails.Builder companyName(String companyName);

        public abstract ShotDetails.Builder companyProfileUrl(String companyProfileUrl);

        public abstract ShotDetails.Builder appName(String appName);

        public abstract ShotDetails.Builder date(LocalDateTime date);

        public abstract ShotDetails.Builder likesCount(Integer likeCount);

        public abstract ShotDetails.Builder bucketCount(Integer bucketCount);

        public abstract ShotDetails.Builder description(String description);

        public abstract ShotDetails.Builder authorId(Integer description);

        public abstract ShotDetails.Builder isLiked(boolean state);

        public abstract ShotDetails.Builder isBucketed(boolean state);

        public abstract ShotDetails.Builder isGif(boolean state);

        public abstract ShotDetails.Builder hdpiImageUrl(String url);

        public abstract ShotDetails.Builder normalImageUrl(String url);

        public abstract ShotDetails.Builder thumbnailUrl(String url);

        public abstract ShotDetails build();
    }

    public static ShotDetails.Builder builder() {
        return new AutoValue_ShotDetails.Builder();
    }

    public static TypeAdapter<ShotDetails> typeAdapter(Gson gson) {
        return new AutoValue_ShotDetails.GsonTypeAdapter(gson);
    }
}
