package co.netguru.android.inbbbox.common.analytics;

import okhttp3.Request;

enum RequestDecoder {
    ADD_SHOT_TO_BUCKET,
    LIKE,
    COMMENT,
    FOLLOW,
    OTHER;

    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String REGEX_ADD_TO_BUCKET = "\\.*buckets/\\.+/shots";
    private static final String REGEX_LIKE_SHOTS = "\\.*shots/\\.+/like";
    private static final String REGEX_FOLLOW = "\\.*users/\\.+/follow";
    private static final String REGEX_COMMENT = "\\.*shots/\\.+/comments";

    static RequestDecoder decodeRequest(Request request) {
        final String method = request.method();
        final String path = request.url().encodedPath();
        if (METHOD_POST.equals(method) && path.matches(REGEX_LIKE_SHOTS))
            return LIKE;
        if (METHOD_POST.equals(method) && path.matches(REGEX_COMMENT))
            return COMMENT;
        if (METHOD_PUT.equals(method) && path.matches(REGEX_ADD_TO_BUCKET))
            return ADD_SHOT_TO_BUCKET;
        if (METHOD_PUT.equals(method) && path.matches(REGEX_FOLLOW))
            return FOLLOW;
        return OTHER;
    }

}
