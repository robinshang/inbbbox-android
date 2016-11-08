package co.netguru.android.inbbbox.api;

import co.netguru.android.inbbbox.model.api.User;
import retrofit2.http.GET;
import rx.Observable;

public interface UserApi {

    @GET("user")
    Observable<User> getAuthenticatedUser();
}
