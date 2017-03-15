package co.netguru.android.inbbbox.data.cache;

public class CacheStrategy {

    public static final String HEADER_CACHE_STRATEGY = "Internal-Cache-Strategy";
    static final String CACHE_STRATEGY_CACHE_LONG = "Cache-Long";
    static final String CACHE_STRATEGY_CACHE_MEDIUM = "Cache-Medium";
    private static final String CACHE_STRATEGY_NO_CACHE = "No-Cache";

    private CacheStrategy() {
    }

    public static String longCache() {
        return CACHE_STRATEGY_CACHE_LONG;
    }

    public static String mediumCache() {
        return CACHE_STRATEGY_CACHE_MEDIUM;
    }

    public static String noCache() {
        return CACHE_STRATEGY_NO_CACHE;
    }
}
