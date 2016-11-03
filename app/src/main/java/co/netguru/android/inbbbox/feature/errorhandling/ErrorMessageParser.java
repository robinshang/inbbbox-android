package co.netguru.android.inbbbox.feature.errorhandling;

import android.content.res.Resources;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;

public class ErrorMessageParser {

    private Resources resources;

    @Inject
    public ErrorMessageParser(Resources resources) {

        this.resources = resources;
    }

    public String getError(Throwable throwable) {
        return throwable.getMessage();
    }

    public String getErrorLabel(ErrorType type) {
        switch (type) {
            case INVALID_USER_INSTANCE:
                return resources.getString(R.string.invalid_user_error);
            case INVALID_OAUTH_URL:
                return resources.getString(R.string.invalid_outh_url);
            default:
                return resources.getString(R.string.undefined_api_error);
        }
    }
}
