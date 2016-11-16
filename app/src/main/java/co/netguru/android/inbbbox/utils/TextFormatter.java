package co.netguru.android.inbbbox.utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

public final class TextFormatter {

    private TextFormatter() {
        throw new AssertionError();
    }

    public static SpannableStringBuilder addDrawableToTextAtFirstSpace(String text, Drawable drawable) {
        final int index = text.indexOf(' ');
        final ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text.substring(0 , index + 1))
                .append(" ")
                .setSpan(imageSpan, builder.length() - 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(text.substring(index));

        return builder;
    }
}