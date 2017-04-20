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
    public ShotsCollection updatePageStatus(boolean hasMoreShots) {
        return update(this, hasMoreShots);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract BucketWithShots.Builder bucket(Bucket bucket);

        public abstract BucketWithShots.Builder shots(List<Shot> shots);

        public abstract BucketWithShots.Builder hasMoreShots(boolean hasMoreShots);

        public abstract BucketWithShots.Builder nextShotPage(int nextShotPage);

        public abstract BucketWithShots build();
    }

    public static BucketWithShots create(Bucket bucket, List<Shot> shots) {
        return create(bucket, shots, true);
    }

    public static BucketWithShots.Builder builder() {
        return new AutoValue_BucketWithShots.Builder();
    }

    public static BucketWithShots create(Bucket bucket, List<Shot> shots, boolean hasMoreShots) {
        return BucketWithShots.builder()
                .bucket(bucket)
                .shots(shots)
                .hasMoreShots(hasMoreShots)
                .nextShotPage(BucketWithShots.FIRST_NEXT_SHOT_PAGE)
                .build();
    }

    public static BucketWithShots update(BucketWithShots bucketWithShots, boolean hasMoreShots) {
        return BucketWithShots.builder()
                .bucket(bucketWithShots.bucket())
                .shots(bucketWithShots.shots())
                .hasMoreShots(hasMoreShots)
                .nextShotPage(bucketWithShots.nextShotPage() + 1)
                .build();
    }
}