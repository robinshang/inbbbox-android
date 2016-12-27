package co.netguru.android.inbbbox.shot.recycler;


import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotSwipeListener {
    void onShotLikeSwipe(Shot shot);

    void onAddShotToBucketSwipe(Shot shot);

    void onCommentShotSwipe(Shot shot);

    void onShotSelected(Shot shot);
}
