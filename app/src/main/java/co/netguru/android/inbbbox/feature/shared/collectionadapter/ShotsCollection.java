package co.netguru.android.inbbbox.feature.shared.collectionadapter;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotsCollection {
    List<Shot> shots();

    boolean hasMoreShots();

    int nextShotPage();

    long getId();

    String getName();

    ShotsCollection updatePageStatus(boolean hasMoreShots);
}
