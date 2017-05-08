package co.netguru.android.inbbbox.app;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public class OkHttpGlideModule implements com.bumptech.glide.module.GlideModule {

    @Inject
    OkHttpClient okHttpClient;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //no-op
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        App.getAppComponent(context).inject(this);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            glide.register(GlideUrl.class, InputStream.class,
                    new com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader.Factory(okHttpClient));
        }
    }
}