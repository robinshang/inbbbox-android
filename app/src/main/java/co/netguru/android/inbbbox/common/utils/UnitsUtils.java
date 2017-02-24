package co.netguru.android.inbbbox.common.utils;

import android.content.res.Resources;

public class UnitsUtils {

    private UnitsUtils() {
        throw new AssertionError();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
