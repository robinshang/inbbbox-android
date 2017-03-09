package co.netguru.android.inbbbox.app;

import com.google.gson.Gson;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.common.retrofit.UpdatedRxJavaCallAdapter;
import co.netguru.android.inbbbox.data.bucket.BucketApi;
import co.netguru.android.inbbbox.data.dribbbleuser.team.TeamApi;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserApi;
import co.netguru.android.inbbbox.data.follower.FollowersApi;
import co.netguru.android.inbbbox.data.like.LikesApi;
import co.netguru.android.inbbbox.data.user.projects.ProjectsApi;
import co.netguru.android.inbbbox.data.session.AuthorizeApi;
import co.netguru.android.inbbbox.data.shot.ShotsApi;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Singleton
    @Provides
    AuthorizeApi provideAuthorizeApi(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                // TODO: 15.12.2016 change to regular call adapter as noted in UpdatedRxJavaCallAdapter
                .addCallAdapterFactory(UpdatedRxJavaCallAdapter.create())
                .baseUrl(Constants.OAUTH.BASE_URL)
                .client(okHttpClient)
                .build()
                .create(AuthorizeApi.class);
    }

    @Singleton
    @Provides
    UserApi provideAuthenticatedUserApi(Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

    @Singleton
    @Provides
    ShotsApi provideShotsApi(Retrofit retrofit) {
        return retrofit.create(ShotsApi.class);
    }

    @Singleton
    @Provides
    LikesApi provideLikesApi(Retrofit retrofit) {
        return retrofit.create(LikesApi.class);
    }

    @Singleton
    @Provides
    FollowersApi provideFollowersApi(Retrofit retrofit) {
        return retrofit.create(FollowersApi.class);
    }

    @Singleton
    @Provides
    BucketApi providesBucketApi(Retrofit retrofit) {
        return retrofit.create(BucketApi.class);
    }

    @Singleton
    @Provides
    TeamApi provideTeamApi(Retrofit retrofit) {
        return retrofit.create(TeamApi.class);
    }

    @Singleton
    @Provides
    ProjectsApi provideProjectsApi(Retrofit retrofit) {
        return retrofit.create(ProjectsApi.class);
    }
}
