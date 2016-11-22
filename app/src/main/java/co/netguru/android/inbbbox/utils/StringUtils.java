package co.netguru.android.inbbbox.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;

public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Spanned getParsedHtmlTextSpanned(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = getHtmlNewApi(html);
        } else {
            result = getHtmlOldApi(html);
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    private static Spanned getHtmlOldApi(String html) {
        return Html.fromHtml(html);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Spanned getHtmlNewApi(String html) {
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
    }

}
