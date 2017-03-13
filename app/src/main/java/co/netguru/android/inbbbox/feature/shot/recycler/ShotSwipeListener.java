package co.netguru.android.inbbbox.feature.shot.recycler;


import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotSwipeListener {
    void onShotLikeSwipe(Shot shot);

    void onLikeAndAddShotToBucketSwipe(Shot shot);

    void onCommentShotSwipe(Shot shot);

    void onFollowUserSwipe(Shot shot);

    void onShotSelected(Shot shot);

    void onStartSwipe(Shot shot);

    void onEndSwipe(Shot shot);
}
