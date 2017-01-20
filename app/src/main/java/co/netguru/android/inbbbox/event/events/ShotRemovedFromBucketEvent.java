package co.netguru.android.inbbbox.event.events;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.Event;

public class ShotRemovedFromBucketEvent implements Event {

    private final Shot shot;

    public ShotRemovedFromBucketEvent(Shot shot) {
        this.shot = shot;
    }

    public Shot getShot() {
        return shot;
    }
}
