package co.netguru.android.inbbbox.utils;

public class Constants {
    public static final int UNDEFINED = -1;


    public static class OAUTH {
        public static final String BASE_URL = "https://dribbble.com/";
        public static final String OAUTH_AUTHORIZE_ENDPOINT = "oauth/authorize";
        public static final String OAUTH_TOKEN_ENDPOINT = "oauth/token";

        public static final String CLIENT_ID_KEY = "client_id";
        public static final String SCOPE_KEY = "scope";
        public static final String STATE_KEY = "state";
        public static final String CLIENT_SECRET_KEY = "client_secret";
        public static final String CODE_KEY = "code";
        public static final String ERROR_KEY = "error";
        public static final String REDIRECT_URI_KEY = "redirect_uri";
    }

    public static class API {

        public static final String HEADER_ACCEPT = "Accept";
        public static final String HEADER_TYPE_JSON = "application/json";
        public static final String HEADER_AUTHORIZATION = "AUTHORIZATION";
        public static final String DRIBBLE_BASE_URL = "https://api.dribbble.com/v1/";
        public static final String SHOTS_KEY_LIST = "list";
        public static final String SHOTS_KEY_TIME_FRAME = "timeframe";
        public static final String SHOTS_KEY_SORT = "sort";
        public static final String SHOTS_KEY_DATE = "date";
        public static final String LIST_PARAM_DEBUTS_PARAM = "debuts";
        public static final String USER_ENDPOINT = "user";
        public static final String SHOTS_ENDPOINT = "shots";
        public static final String FOLLOWING_SHOTS_ENDPOINT = "user/following/shots";
    }

    public class Db {
        public static final String TOKEN_KEY = "db_token";
        public static final String CURRENT_USER_KEY = "db_current_user";
        public static final String SETTINGS_KEY = "db_settings";
    }

    public class Notification {
        public static final int NOTIFICATION_ID = 0;
    }
}
