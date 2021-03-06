package co.netguru.android.inbbbox.feature.shot.detail;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.data.shot.model.api.CommentEntity;

@AutoValue
public abstract class Comment {

    public abstract long id();

    public abstract String author();

    public abstract String authorAvatarUrl();

    public abstract ZonedDateTime date();

    public abstract String text();

    public abstract Boolean isCurrentUserAuthor();

    public static Comment create(CommentEntity commentEntity, boolean isCurrentUserAuthor) {
        return Comment.builder()
                .id(commentEntity.getId())
                .author(commentEntity.getUser().name())
                .authorAvatarUrl(commentEntity.getUser().avatarUrl())
                .text(commentEntity.getBody())
                .date(commentEntity.getCreatedAt())
                .isCurrentUserAuthor(isCurrentUserAuthor)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Comment.Builder id(long id);

        public abstract Comment.Builder author(String author);

        public abstract Comment.Builder authorAvatarUrl(String authorAvatarUrl);

        public abstract Comment.Builder date(ZonedDateTime date);

        public abstract Comment.Builder text(String text);

        public abstract Comment.Builder isCurrentUserAuthor(Boolean isCurrentUserAuthor);

        public abstract Comment build();
    }

    public static Comment.Builder builder() {
        return new AutoValue_Comment.Builder();
    }

    public static TypeAdapter<Comment> typeAdapter(Gson gson) {
        return new AutoValue_Comment.GsonTypeAdapter(gson);
    }
}
