package co.netguru.android.inbbbox.feature.shared.collectionadapter;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface CollectionClickListener<C extends ShotsCollection> {
    void onCollectionClick(C collectionWithShots);

    void onShotClick(Shot shot, C collectionWithShots);
}
