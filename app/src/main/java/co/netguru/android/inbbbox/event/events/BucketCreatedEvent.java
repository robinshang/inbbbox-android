package co.netguru.android.inbbbox.event.events;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.event.Event;

public class BucketCreatedEvent implements Event {

    private final Bucket bucket;

    public BucketCreatedEvent(Bucket bucket) {
        this.bucket = bucket;
    }

    public Bucket getBucket() {
        return bucket;
    }
}
