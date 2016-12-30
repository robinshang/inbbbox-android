package co.netguru.android.inbbbox.data.db.mappers;

import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.data.db.BucketDB;

public class BucketDbMapper {

    private BucketDbMapper() {
        throw new AssertionError();
    }

    public static BucketDB createNewBucket(String name, String description) {
        return new BucketDB(null, name, description, 0, ZonedDateTime.now());
    }
}
