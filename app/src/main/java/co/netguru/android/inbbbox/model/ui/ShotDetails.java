package co.netguru.android.inbbbox.model.ui;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;


@AutoValue
public abstract class ShotDetails {
    public abstract Integer id();

    public abstract String title();

    public abstract String userAvatarUrl();

    public abstract String authorName();

    public abstract String authorUrl();

    public abstract String companyName();

    public abstract String companyProfileUrl();

    public abstract String appName();

    public abstract String date();

    public abstract Integer likesCount();

    public abstract Integer bucketCount();

    public abstract String description();

    @Nullable
    public abstract List<Comment> comments();

    public abstract Integer authorId();

    public abstract boolean isBucketed();

    public abstract boolean isLiked();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract ShotDetails.Builder id(Integer id);

        public abstract ShotDetails.Builder title(String title);

        public abstract ShotDetails.Builder comments(List<Comment> comments);

        public abstract ShotDetails.Builder userAvatarUrl(String userAvatarUrl);

        public abstract ShotDetails.Builder authorName(String authorName);

        public abstract ShotDetails.Builder authorUrl(String authorUrl);

        public abstract ShotDetails.Builder companyName(String companyName);

        public abstract ShotDetails.Builder companyProfileUrl(String companyProfileUrl);

        public abstract ShotDetails.Builder appName(String appName);

        public abstract ShotDetails.Builder date(String date);

        public abstract ShotDetails.Builder likesCount(Integer likeCount);

        public abstract ShotDetails.Builder bucketCount(Integer bucketCount);

        public abstract ShotDetails.Builder description(String description);

        public abstract ShotDetails.Builder authorId(Integer description);

        public abstract ShotDetails.Builder isLiked(boolean state);

        public abstract ShotDetails.Builder isBucketed(boolean state);

        public abstract ShotDetails build();
    }

    public static ShotDetails.Builder builder() {
        return new AutoValue_ShotDetails.Builder();
    }

    public static TypeAdapter<ShotDetails> typeAdapter(Gson gson) {
        return new AutoValue_ShotDetails.GsonTypeAdapter(gson);
    }
}
