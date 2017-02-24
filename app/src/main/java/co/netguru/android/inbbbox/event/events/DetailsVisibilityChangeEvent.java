package co.netguru.android.inbbbox.event.events;

import co.netguru.android.inbbbox.event.Event;

public class DetailsVisibilityChangeEvent implements Event {

    private final boolean isDetailsVisible;


    public DetailsVisibilityChangeEvent(boolean isDetailsVisible) {
        this.isDetailsVisible = isDetailsVisible;
    }

    public boolean isDetailsVisible() {
        return isDetailsVisible;
    }
}
