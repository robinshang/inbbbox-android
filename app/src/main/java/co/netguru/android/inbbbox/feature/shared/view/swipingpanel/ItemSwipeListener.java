package co.netguru.android.inbbbox.feature.shared.view.swipingpanel;

public interface ItemSwipeListener {
    void onLeftSwipe();

    void onLeftLongSwipe();

    void onRightSwipe();

    void onRightLongSwipe();

    void onSwipeProgress(int positionX, int swipeLimit);

    void onStartSwipe();

    void onEndSwipe();
}
