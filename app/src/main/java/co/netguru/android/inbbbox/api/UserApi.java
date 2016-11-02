package co.netguru.android.inbbbox.api;

import co.netguru.android.inbbbox.data.models.User;
import retrofit2.http.GET;
import rx.Observable;

public interface UserApi {

    @GET("/v1/user")
    Observable<User> getAuthenticatedUser();
}
