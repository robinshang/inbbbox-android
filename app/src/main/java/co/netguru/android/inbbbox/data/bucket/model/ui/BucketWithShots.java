package co.netguru.android.inbbbox.data.bucket.model.ui;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

import co.netguru.android.inbbbox.data.Cacheable;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;


@AutoValue
public abstract class BucketWithShots implements Parcelable, Cacheable {
    public abstract Bucket bucket();

    public abstract List<Shot> shots();

    public static BucketWithShots create(Bucket bucket, List<Shot> shots) {
        return new AutoValue_BucketWithShots(bucket, shots);
    }

    @Override
    public long getId() {
        return bucket().id();
    }
}