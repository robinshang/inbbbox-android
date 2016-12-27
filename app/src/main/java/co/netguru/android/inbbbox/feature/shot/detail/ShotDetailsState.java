package co.netguru.android.inbbbox.feature.shot.detail;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ShotDetailsState {

    public abstract boolean isLiked();

    public abstract boolean isBucketed();

    public abstract List<Comment> comments();

    public static ShotDetailsState.Builder builder(){
        return new AutoValue_ShotDetailsState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract ShotDetailsState.Builder isLiked(boolean isLiked);

        public abstract ShotDetailsState.Builder isBucketed(boolean isLiked);

        public abstract ShotDetailsState.Builder comments(List<Comment> comments);

        public abstract ShotDetailsState build();
    }

    public static ShotDetailsState create(boolean isLiked,
                                          boolean isBucketed,
                                          List<Comment> comments){
        return ShotDetailsState.builder()
                .isLiked(isLiked)
                .isBucketed(isBucketed)
                .comments(comments)
                .build();

    }
}
