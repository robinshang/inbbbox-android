package co.netguru.android.inbbbox.data.api;

import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.utils.Constants;
import retrofit2.http.GET;
import rx.Observable;

public interface UserApi {

    @GET(Constants.API.USER_ENDPOINT)
    Observable<User> getAuthenticatedUser();
}
