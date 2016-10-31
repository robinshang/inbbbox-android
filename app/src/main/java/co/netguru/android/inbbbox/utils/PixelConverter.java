package co.netguru.android.inbbbox.utils;

import android.content.Context;
import android.util.TypedValue;

public class PixelConverter {
    public static int convertToPx(int number, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, number,
                context.getResources().getDisplayMetrics());
    }
}
