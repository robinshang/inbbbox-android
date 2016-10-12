package co.netguru.android.inbbbox.application.configuration;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.models.Token;
import dagger.Module;
import dagger.Provides;

@Module
public class RequestInterceptorModule {

    private Token token;

    public RequestInterceptorModule(Token token) {

        this.token = token;
    }

    @Provides
    @Singleton
    public RequestInterceptor providesRequestInterceptor() {
        return new RequestInterceptor(token);
    }
}
