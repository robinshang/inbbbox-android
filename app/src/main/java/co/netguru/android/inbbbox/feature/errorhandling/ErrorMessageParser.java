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

    public String getError(int errorCode) {
        return resources.getString(R.string.undefined_api_error);
    }

    public String getError(Throwable throwable) {
        return throwable.getMessage();
    }

    public String getErrorLabel(ErrorType type) {
        String label = resources.getString(R.string.undefined_api_error);
        switch (type) {
            case INVALID_USER_INSTANCE:
                label = resources.getString(R.string.invalid_user_error);
                break;
            case INVALID_OAURH_URL:
                label = resources.getString(R.string.invalid_outh_uri);
                break;
        }
        return label;
    }
}
