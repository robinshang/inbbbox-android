package co.netguru.android.inbbbox.common.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

public final class TextFormatterUtil {

    private TextFormatterUtil() {
        throw new AssertionError();
    }

    public static SpannableStringBuilder addDrawableToTextAtFirstSpace(String text, Drawable drawable) {
        final int index = text.indexOf(' ');
        final ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text.substring(0, index + 1))
                .append(" ")
                .setSpan(imageSpan, builder.length() - 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(text.substring(index));

        return builder;
    }

    public static SpannableStringBuilder addDrawableBetweenStrings(String firstsString, String secondString,
                                                                   Drawable drawable) {
        final ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        final SpannableStringBuilder builder = new SpannableStringBuilder(firstsString);
        builder.append("  ")
                .setSpan(imageSpan, builder.length() - 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" ").append(secondString);

        return builder;
    }

    public static SpannableStringBuilder changeColourOfConcatenatedWord(String base, String wordToConcat,
                                                                        @ColorInt int color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(base);
        spannableStringBuilder
                .append(' ')
                .append(wordToConcat)
                .setSpan(new ForegroundColorSpan(color),
                        spannableStringBuilder.length() - wordToConcat.length(), spannableStringBuilder.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableStringBuilder;

    }
}
