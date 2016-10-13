/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.utils;

public class Constants {
    public static final int UNDEFINED = -1;

    public static class OAUTH {

       public static final String OAUTH_BASE_URL = "https://dribbble.com/oauth/";
       public static final String OAUTH_AUTHORIZE_ENDPOINT = "authorize";
       public static final String OAUTH_TOKEN_ENDPOINT = "token";

       public static final String CLIENT_ID_KEY = "client_id";
       public static final String SCOPE_KEY = "scope";
       public static final String STATE_KEY = "state";
       public static final String CLIENT_SECRET_KEY = "client_secret";
       public static final String CODE_KEY = "code";
       public static final String ERROR_KEY = "key";
       public static final String REDIRECT_URI_KEY = "redirect_uri";
    }

   public static class API {
      public static final String HEDAER_ACCEPT = "Accept";
      public static final String HEADER_TYPE_JSON = "application/json";
      public static final String HEADER_AUTHORIZATION = "Authorization";
   }
}
