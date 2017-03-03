package co.netguru.android.inbbbox;

public final class Constants {

    public static final int UNDEFINED = -1;

    private Constants() {
        throw new AssertionError();
    }

    public static final class OAUTH {
        public static final String BASE_URL = "https://dribbble.com/";
        public static final String SIGN_UP_ENDPOINT = "signup";
        public static final String OAUTH_AUTHORIZE_ENDPOINT = "oauth/authorize";
        public static final String OAUTH_TOKEN_ENDPOINT = "oauth/token";

        public static final String CLIENT_ID_KEY = "client_id";
        public static final String SCOPE_KEY = "scope";
        public static final String STATE_KEY = "state";
        public static final String CLIENT_SECRET_KEY = "client_secret";
        public static final String CODE_KEY = "code";
        public static final String ERROR_KEY = "error";
        public static final String INBBBOX_SCOPE = "public+write+comment";
        public static final String INBBBOX_GUEST_SCOPE = "public";
        public static final String INBBBOX_DEFAULT_TOKEN_TYPE = "Bearer";
        public static final String URI_SCHEME = "https";
        public static final String URI_AUTHORITY = "dribbble.com";

        private OAUTH() {
            throw new AssertionError();
        }
    }

    public static final class API {
        public static final String DRIBBLE_BASE_URL = "https://api.dribbble.com/v1/";
        public static final String SHOTS_KEY_LIST = "list";
        public static final String SHOTS_KEY_SORT = "sort";
        public static final String SHOTS_KEY_DATE = "creationDate";
        public static final String LIST_PARAM_DEBUTS_PARAM = "debuts";
        public static final String LIST_PARAM_SORT_RECENT_PARAM = "recent";

        private API() {
            throw new AssertionError();
        }
    }
}
