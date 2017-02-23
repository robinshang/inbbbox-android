package co.netguru.android.inbbbox.common.analytics;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {

    private Context context;

    public AnalyticsModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    FirebaseAnalytics provideFirebaseAnalytics() {
        return FirebaseAnalytics.getInstance(context);
    }

    @Singleton
    @Provides
    AnalyticsEventLogger provideAnalyticsEventLogger(FirebaseAnalytics firebaseAnalytics) {
        return new FirebaseAnalyticsEventLogger(firebaseAnalytics);
    }

    @Singleton
    @Provides
    AnalyticsInterceptor provideAnalyticsInterceptor(AnalyticsEventLogger eventLogger) {
        return new AnalyticsInterceptor(eventLogger);
    }
}
