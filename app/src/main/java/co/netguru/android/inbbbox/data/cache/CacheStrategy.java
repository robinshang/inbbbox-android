package co.netguru.android.inbbbox.data.cache;

public class CacheStrategy {

    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    static final String CACHE_CONTROL_NO_CACHE = "no-cache";
    static final String CACHE_CONTROL_MAX_AGE = "max-age=";
    static final int MEDIUM_CACHE_TIME = 60 * 60 * 24; // 24 hours
    static final int LONG_CACHE_TIME = 60 * 60 * 72; // 72 hours

    private CacheStrategy() {
    }

    public static String longCache() {
        return CACHE_CONTROL_MAX_AGE+LONG_CACHE_TIME;
    }

    public static String mediumCache() {
        return CACHE_CONTROL_MAX_AGE+MEDIUM_CACHE_TIME;
    }

    public static String noCache() {
        return CACHE_CONTROL_NO_CACHE;
    }
}
