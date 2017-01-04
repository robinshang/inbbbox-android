package co.netguru.android.inbbbox.feature.shot.detail;

import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.google.auto.value.AutoValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@AutoValue
public abstract class ShotDetailsRequest implements Parcelable {

    @ShotDetailsType.DetailsType
    public abstract int detailsType();

    public abstract long id();

    public abstract boolean isCommentModeEnabled();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder detailsType(int detailsType);

        public abstract Builder id(long id);

        public abstract Builder isCommentModeEnabled(boolean isCommentModeEnabled);

        public abstract ShotDetailsRequest build();
    }

    public static Builder builder() {
        return new $AutoValue_ShotDetailsRequest.Builder()
                .isCommentModeEnabled(false)
                .id(0);
    }
}
