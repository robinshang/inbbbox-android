package co.netguru.android.inbbbox.di.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.Constants;
import dagger.Module;
import dagger.Provides;

@Module
public class LikesFragmentModule {

    @Provides
    @FragmentScope
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    @FragmentScope
    GridLayoutManager provideGridLayoutManager(Context context) {
        return new GridLayoutManager(context, Constants.View.LIKE_FRAGMENT_COLUMN_COUNT);
    }
}
