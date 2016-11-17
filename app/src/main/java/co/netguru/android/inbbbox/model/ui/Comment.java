package co.netguru.android.inbbbox.model.ui;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.model.api.CommentEntity;

@AutoValue
public abstract class Comment {
    public abstract String author();

    public abstract String authorAvatarUrl();

    public abstract LocalDateTime date();

    public abstract String text();

    public static Comment create(CommentEntity commentEntity) {
        return Comment.builder()
                .author(commentEntity.getUser().name())
                .authorAvatarUrl(commentEntity.getUser().avatarUrl())
                .text(commentEntity.getBody())
                .date(commentEntity.getCreatedAt())
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Comment.Builder author(String author);

        public abstract Comment.Builder authorAvatarUrl(String authorAvatarUrl);

        public abstract Comment.Builder date(LocalDateTime date);

        public abstract Comment.Builder text(String text);

        public abstract Comment build();
    }

    public static Comment.Builder builder() {
        return new AutoValue_Comment.Builder();
    }

    public static TypeAdapter<Comment> typeAdapter(Gson gson) {
        return new AutoValue_Comment.GsonTypeAdapter(gson);
    }
}
