package co.netguru.android.inbbbox.data.bucket.model.ui;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

import co.netguru.android.inbbbox.data.Cacheable;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.collectionadapter.ShotsCollection;

@AutoValue
public abstract class BucketWithShots implements Parcelable, Cacheable, ShotsCollection {
    public abstract Bucket bucket();

    public static BucketWithShots create(Bucket bucket, List<Shot> shots) {
        return new AutoValue_BucketWithShots(shots, bucket);
    }

    @Override
    public long getId() {
        return bucket().id();
    }

    @Override
    public String getName() {
        return bucket().name();
    }
}