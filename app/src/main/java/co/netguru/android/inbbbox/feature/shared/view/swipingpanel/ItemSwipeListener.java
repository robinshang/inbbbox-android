package co.netguru.android.inbbbox.feature.shared.view.swipingpanel;

public interface ItemSwipeListener {
    void onLeftSwipe();

    void onLeftLongSwipe();

    void onRightSwipe();

    void onRightLongSwipe();

    void onLeftSwipeActivate(boolean isActive);

    void onLeftLongSwipeActivate(boolean isActive);

    void onRightSwipeActivate(boolean isActive);

    void onRightLongSwipeActivate(boolean isActive);

    void onSwipeProgress(int positionX, int swipeLimit);

    void onStartSwipe();

    void onEndSwipe();
}
