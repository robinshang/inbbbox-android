package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;

@AutoValue
public abstract class BucketWithShots implements Parcelable {
    public abstract Bucket bucket();

    public abstract List<Shot> shots();

    public static BucketWithShots create(Bucket bucket, List<Shot> shots) {
        return new AutoValue_BucketWithShots(bucket, shots);
    }

}