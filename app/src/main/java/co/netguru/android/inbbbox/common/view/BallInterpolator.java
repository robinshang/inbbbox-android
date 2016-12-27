package co.netguru.android.inbbbox.common.view;

import android.view.animation.Interpolator;

// this interpolator is android Anticipate Interpolator reversed
public class BallInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float t) {
        float tension = 4;
        t = 1 - t;
        return t * t * ((tension + 1) * t - tension);
    }
}