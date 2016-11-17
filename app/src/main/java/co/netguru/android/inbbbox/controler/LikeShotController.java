package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.HttpsURLConnection;

import co.netguru.android.inbbbox.api.LikesApi;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Completable;
import rx.Observable;

@Singleton
public class LikeShotController {

    private final LikesApi likesApi;

    @Inject
    LikeShotController(LikesApi likesApi) {
        this.likesApi = likesApi;
    }

    public Completable likeShot(long id) {
        return likesApi.likeShot(id);
    }
}