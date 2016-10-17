package co.netguru.android.inbbbox.feature.errorhandling;

import android.content.res.Resources;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;

public class ApiErrorParser {

    private Resources resources;

    @Inject
    public ApiErrorParser(Resources resources) {

        this.resources = resources;
    }

    public String getApiError(int errorCode) {
        return resources.getString(R.string.undefined_api_error);
    }

    public String getApiError(Throwable throwable) {
        return throwable.getMessage();
    }

    public String getErrorLabel(ErrorType type) {
        String label = resources.getString(R.string.undefined_api_error);
        switch (type) {
            case INVALID_USER_INSTANCE:
                label = resources.getString(R.string.invalid_user_error);
                break;
        }
        return label;
    }
}
