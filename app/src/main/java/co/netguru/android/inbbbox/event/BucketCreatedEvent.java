package co.netguru.android.inbbbox.event;

import co.netguru.android.inbbbox.model.api.Bucket;

public class BucketCreatedEvent implements Event {

    private final Bucket bucket;

    public BucketCreatedEvent(Bucket bucket) {
        this.bucket = bucket;
    }

    public Bucket getBucket() {
        return bucket;
    }
}
