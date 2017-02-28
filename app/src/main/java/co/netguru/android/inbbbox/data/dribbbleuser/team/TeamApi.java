package co.netguru.android.inbbbox.data.dribbbleuser.team;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

public interface TeamApi {

    @GET("teams/{team}/members")
    Single<List<UserEntity>> getTeamMembers(@Path("team") long teamId,
                                            @Query("page") int pageNumber,
                                            @Query("per_page") int pageCount);


    @GET("users/{user}/teams")
    Single<List<UserEntity>> getUserTeams(@Path("user") long userId,
                                          @Query("page") int pageNumber,
                                          @Query("per_page") int pageCount);
}
