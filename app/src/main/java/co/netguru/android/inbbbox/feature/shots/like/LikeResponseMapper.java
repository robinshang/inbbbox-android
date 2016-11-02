package co.netguru.android.inbbbox.feature.shots.like;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.api.LikesApi;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

@FragmentScope
public final class LikeResponseMapper {

    private final LikesApi likesApi;

    @Inject
    LikeResponseMapper(LikesApi likesApi) {
        this.likesApi = likesApi;
    }

    public Observable<Boolean> likeShot(long id) {
        return likesApi.likeShot(id)
                .map(this::mapLikeResponse);
    }

    private boolean mapLikeResponse(Response<ResponseBody> response) {
        return response.code() == HttpsURLConnection.HTTP_CREATED;
    }
}
