package co.netguru.android.inbbbox.feature.shared.view;

import android.view.animation.Interpolator;

// this interpolator is android Anticipate Interpolator reversed
public class BallInterpolator implements Interpolator {

    @Override
    public float getInterpolation(final float t) {
        float tension = 4;
        float factor = 1 - t;
        return factor * factor * ((tension + 1) * factor - tension);
    }
}