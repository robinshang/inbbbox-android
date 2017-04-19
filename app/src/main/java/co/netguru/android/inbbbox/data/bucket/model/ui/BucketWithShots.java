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

    private static final int FIRST_NEXT_SHOT_PAGE = 2;

    public static BucketWithShots create(Bucket bucket, List<Shot> shots) {
        return create(bucket, shots, true);
    }

    public static BucketWithShots create(Bucket bucket, List<Shot> shots, boolean hasMoreShots) {
        // na pewno has more zamiast true ?
        return new AutoValue_BucketWithShots(shots, hasMoreShots, BucketWithShots.FIRST_NEXT_SHOT_PAGE, bucket);
    }

    public static BucketWithShots update(BucketWithShots bucketWithShots, boolean hasMoreShots) {
        return new AutoValue_BucketWithShots(bucketWithShots.shots(), hasMoreShots,
                bucketWithShots.nextShotPage() + 1, bucketWithShots.bucket());

    }

    public abstract Bucket bucket();

    @Override
    public long getId() {
        return bucket().id();
    }

    @Override
    public String getName() {
        return bucket().name();
    }

    @Override
    public BucketWithShots updatePageStatus(boolean hasMoreShots) {
        return update(this, hasMoreShots);
    }
}