package co.netguru.android.inbbbox.feature.shot.recycler;

@FunctionalInterface
public interface DetailsVisibilityChangeEmitter {
    void setListener(DetailsVisibilityChangeListener listener);
}
