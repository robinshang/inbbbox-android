package co.netguru.android.inbbbox.feature.shared.collectionadapter;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotsCollection {
    List<Shot> shots();
    long getId();
    String getName();
}
