package co.netguru.android.inbbbox.common.other;

import android.os.Vibrator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VibrationController {

    private final Vibrator vibrator;

    @Inject
    public VibrationController(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    public void vibrate(int milliseconds) {
        vibrator.vibrate(milliseconds);
    }
}
