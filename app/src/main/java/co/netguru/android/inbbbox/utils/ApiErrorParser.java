package co.netguru.android.inbbbox.utils;

import android.content.res.Resources;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;

public class ApiErrorParser {

    private Resources resources;

    @Inject
    public ApiErrorParser(Resources resources){

        this.resources = resources;
    }

    public String getApiError(int errorCode){
        return resources.getString(R.string.undefined_api_error);
    }
}
