package co.netguru.android.inbbbox.data.shot.model.ui;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.shot.model.db.ShotDB;

@AutoValue
public abstract class Shot implements Parcelable, ShotImage {

    public abstract long id();

    @Nullable
    public abstract String title();

    public abstract ZonedDateTime creationDate();

    @Nullable
    public abstract String projectUrl();

    public abstract Integer likesCount();

    public abstract Integer bucketCount();

    public abstract Integer commentsCount();

    @Nullable
    public abstract String description();

    public abstract boolean isGif();

    @Override
    @Nullable
    public abstract String hiDpiImageUrl();

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

    public static Builder update(Shot shot) {
        return Shot.builder()
                .id(shot.id())
                .title(shot.title())
                .author(shot.author())
                .team(shot.team())
                .projectUrl(shot.projectUrl())
                .likesCount(shot.likesCount())
                .bucketCount(shot.bucketCount())
                .description(shot.description())
                .isGif(shot.isGif())
                .creationDate(shot.creationDate())
                .hiDpiImageUrl(shot.hiDpiImageUrl())
                .normalImageUrl(shot.normalImageUrl())
                .thumbnailUrl(shot.thumbnailUrl())
                .isLiked(shot.isLiked())
                .commentsCount(shot.commentsCount())
                .isBucketed(shot.isBucketed());
    }

    public static Shot fromDB(ShotDB shotDB) {
        return Shot.builder()
                .id(shotDB.getId())
                .author(shotDB.getUser() != null ? User.fromDB(shotDB.getUser()) : null)
                .title(shotDB.getTitle())
                .creationDate(shotDB.getCreationDate())
                .description(shotDB.getDescription())
                .bucketCount(shotDB.getBucketCount())
                .likesCount(shotDB.getLikesCount())
                .team(shotDB.getTeam() != null ? Team.fromDB(shotDB.getTeam()) : null)
                .isGif(shotDB.getIsGif())
                .isLiked(shotDB.getIsLiked())
                .isBucketed(shotDB.getIsBucketed())
                .hiDpiImageUrl(shotDB.getHiDpiImageUrl())
                .normalImageUrl(shotDB.getNormalImageUrl())
                .thumbnailUrl(shotDB.getThumbnailUrl())
                .commentsCount(shotDB.getCommentsCount())
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Shot.Builder id(long id);

        public abstract Shot.Builder title(String title);

        public abstract Shot.Builder team(Team team);

        public abstract Shot.Builder projectUrl(String url);

        public abstract Shot.Builder likesCount(Integer likeCount);

        public abstract Shot.Builder bucketCount(Integer bucketCount);

        public abstract Shot.Builder creationDate(ZonedDateTime dateTime);

        public abstract Shot.Builder description(String description);

        public abstract Shot.Builder isLiked(boolean state);

        public abstract Shot.Builder isBucketed(boolean state);

        public abstract Shot.Builder isGif(boolean state);

        public abstract Shot.Builder hiDpiImageUrl(String url);

        public abstract Shot.Builder normalImageUrl(String url);

        public abstract Shot.Builder thumbnailUrl(String url);

        public abstract Shot.Builder author(User user);

        public abstract Shot.Builder commentsCount(Integer commentsCount);

        public abstract Shot build();
    }

    public static Shot.Builder builder() {
        return new AutoValue_Shot.Builder();
    }

    public static TypeAdapter<Shot> typeAdapter(Gson gson) {
        return new AutoValue_Shot.GsonTypeAdapter(gson);
    }

    public static Shot create(ShotEntity shotEntity) {
        return Shot.builder()
                .id(shotEntity.id())
                .author(shotEntity.user() != null ? User.create(shotEntity.user()) : null)
                .title(shotEntity.title())
                .creationDate(shotEntity.createdAt())
                .description(shotEntity.description())
                .bucketCount(shotEntity.bucketsCount())
                .likesCount(shotEntity.likesCount())
                .team(shotEntity.team() != null ? Team.create(shotEntity.team()) : null)
                .isGif(shotEntity.animated())
                .isLiked(false)
                .isBucketed(false)
                .hiDpiImageUrl(shotEntity.image().hiDpiImageUrl())
                .normalImageUrl(shotEntity.image().normalImageUrl())
                .thumbnailUrl(shotEntity.image().thumbnailUrl())
                .commentsCount(shotEntity.commentsCount())
                .build();
    }

    public static List<Shot> createList(List<ShotEntity> shotEntityList) {
        List<Shot> shots = new ArrayList<>();
        for(ShotEntity shotEntity : shotEntityList) {
            shots.add(Shot.create(shotEntity));
        }
        return shots;
    }
}
