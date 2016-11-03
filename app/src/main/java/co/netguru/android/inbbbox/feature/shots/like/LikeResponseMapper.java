package co.netguru.android.inbbbox.feature.shots.like;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.api.LikesApi;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

@FragmentScope
public final class LikeResponseMapper {

    private final LikesApi likesApi;

    @Inject
    LikeResponseMapper(LikesApi likesApi) {
        this.likesApi = likesApi;
    }

    public Observable<Void> likeShot(long id) {
        return likesApi.likeShot(id)
                .flatMap(this::mapLikeResponse);
    }

    private Observable<Void> mapLikeResponse(Response<ResponseBody> response) {
        if (response.code() == HttpsURLConnection.HTTP_CREATED) {
            return Observable.empty();
        }
        return Observable.error(new HttpException(response));
    }
}
