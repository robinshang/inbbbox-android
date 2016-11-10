package co.netguru.android.inbbbox.di.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import co.netguru.android.commons.di.FragmentScope;

import co.netguru.android.inbbbox.feature.followers.FollowersFragment;
import dagger.Module;
import dagger.Provides;

@Module
public class FollowersFragmentModule {

    @Provides
    @FragmentScope
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    @FragmentScope
    GridLayoutManager provideGridLayoutManager(Context context) {
        return new GridLayoutManager(context, FollowersFragment.GRID_VIEW_COLUMN_COUNT);
    }
}
