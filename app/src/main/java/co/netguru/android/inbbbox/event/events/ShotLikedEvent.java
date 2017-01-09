package co.netguru.android.inbbbox.event.events;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.event.Event;

public class ShotLikedEvent implements Event {

    private final Shot shot;
    private final boolean newShotLikeState;

    public ShotLikedEvent(Shot shot, boolean newShotLikeState) {
        this.shot = shot;
        this.newShotLikeState = newShotLikeState;
    }

    public Shot getShot() {
        return shot;
    }

    public boolean getNewShotLikeState() {
        return  newShotLikeState;
    }
}
