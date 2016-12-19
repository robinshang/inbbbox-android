package co.netguru.android.inbbbox.feature.details;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ShotDetailsRequest implements Parcelable {

    public static ShotDetailsRequest create(ShotDetailsType type, boolean isCommentModeEnabled) {
        return new AutoValue_ShotDetailsRequest(type, 0, isCommentModeEnabled);
    }
    public static ShotDetailsRequest create(ShotDetailsType type, long id) {
        return new AutoValue_ShotDetailsRequest(type, id, false);
    }

    public static ShotDetailsRequest create(ShotDetailsType type) {
        return new AutoValue_ShotDetailsRequest(type, 0, false);
    }

    public abstract ShotDetailsType detailsType();

    public abstract long id();

    public abstract boolean isCommentModeEnabled();
}
