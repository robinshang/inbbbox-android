package co.netguru.android.inbbbox.di.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import co.netguru.android.inbbbox.feature.followers.details.FollowerDetailsFragment;
import co.netguru.android.inbbbox.feature.followers.details.adapter.FollowerDetailsAdapter;
import co.netguru.android.inbbbox.view.ShotClickListener;
import dagger.Module;
import dagger.Provides;

@Module
public class FollowerDetailsFragmentModule {

    private final ShotClickListener shotClickListener;

    public FollowerDetailsFragmentModule(ShotClickListener shotClickListener) {

        this.shotClickListener = shotClickListener;
    }

    @Provides
    @FragmentScope
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    @FragmentScope
    GridLayoutManager provideGridLayoutManager(Context context) {
        return new GridLayoutManager(context, FollowerDetailsFragment.GRID_VIEW_COLUMN_COUNT);
    }

    @Provides
    @FragmentScope
    FollowerDetailsAdapter provideShotDetailsAdapter() {
        return new FollowerDetailsAdapter(shotClickListener);
    }

}
