package co.netguru.android.inbbbox.data.api;

import co.netguru.android.inbbbox.data.models.User;
import retrofit2.http.GET;
import rx.Observable;

public interface UserApi {

    @GET("user")
    Observable<User> getAuthenticatedUser();
}
