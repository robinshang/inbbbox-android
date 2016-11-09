package co.netguru.android.inbbbox.di.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import co.netguru.android.commons.di.FragmentScope;

import co.netguru.android.inbbbox.Constants;
import dagger.Module;
import dagger.Provides;

@Module
public class FollowersFragmentModule {

    private final Context context;

    public FollowersFragmentModule(Context context) {
        this.context = context;
    }

    @Provides
    @FragmentScope
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(context);
    }

    @Provides
    @FragmentScope
    GridLayoutManager provideGridLayoutManager() {
        return new GridLayoutManager(context, Constants.View.FOLLOWERS_FRAGMENT_COLUMN_COUNT);
    }
}
