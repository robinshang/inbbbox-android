package co.netguru.android.inbbbox.view.swipingpanel;

public interface ItemSwipeListener {
    void onLeftSwipe();

    void onLeftLongSwipe();

    void onRightSwipe();

    void onLeftSwipeActivate(boolean isActive);

    void onLeftLongSwipeActivate(boolean isActive);

    void onRightSwipeActivate(boolean isActive);
}
