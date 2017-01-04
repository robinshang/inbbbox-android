package co.netguru.android.inbbbox.feature.shot.detail;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ShotDetailsType {
    public static final int DEFAULT = 0;
    public static final int ADD_TO_BUCKET = 1;
    public static final int LIKES = 2;
    public static final int BUCKET = 3;
    public static final int USER = 4;

    private ShotDetailsType() {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ShotDetailsType.DEFAULT, ShotDetailsType.ADD_TO_BUCKET, ShotDetailsType.LIKES,
            ShotDetailsType.BUCKET, ShotDetailsType.USER})
    public @interface  DetailsType {}

}
