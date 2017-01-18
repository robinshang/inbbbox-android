package co.netguru.android.inbbbox.feature.shot.detail;

@FunctionalInterface
public interface BucketedStatusChangeEmitter {

    void setListener(BucketedStatusChangeListener bucketedStatusChangeListener);
}
