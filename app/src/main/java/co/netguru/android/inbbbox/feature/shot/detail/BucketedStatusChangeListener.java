package co.netguru.android.inbbbox.feature.shot.detail;

@FunctionalInterface
public interface BucketedStatusChangeListener {

    void onBucketedStatusChanged(boolean isBucketed);
}
