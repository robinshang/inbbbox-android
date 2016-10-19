package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;
import android.net.Uri;

import java.util.UUID;

import javax.inject.Inject;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;

import static co.netguru.android.inbbbox.utils.Constants.OAUTH;

public class OauthUriProvider {

    private Resources resources;

    private String stateString;

    @Inject
    public OauthUriProvider(Resources resources) {

        this.resources = resources;
    }

    public Observable<String> getOauthAuthorizeUriString() {
        initStateString();
        return Observable.just(getAuthorizeUri());
    }

    private void initStateString() {
        stateString = UUID.randomUUID().toString();
    }

    private String getAuthorizeUri() {
        return new StringBuilder(OAUTH.BASE_URL + OAUTH.OAUTH_AUTHORIZE_ENDPOINT)
                .append("?")
                .append(OAUTH.CLIENT_ID_KEY)
                .append("=")
                .append(BuildConfig.DRIBBBLE_CLIENT_KEY)
                .append("&")
                .append(OAUTH.SCOPE_KEY)
                .append("=")
                .append(getStringValue(R.string.dribbleScope))
                .append("&")
                .append(OAUTH.STATE_KEY)
                .append("=")
                .append(stateString)
                .toString();
    }

    private String getStringValue(Integer resId) {
        return resources.getString(resId);
    }
}
