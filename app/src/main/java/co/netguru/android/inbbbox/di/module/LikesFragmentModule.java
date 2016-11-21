package co.netguru.android.inbbbox.di.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.likes.LikesFragment;
import co.netguru.android.inbbbox.feature.likes.adapter.LikeClickListener;
import co.netguru.android.inbbbox.feature.likes.adapter.LikesAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class LikesFragmentModule {

    private LikeClickListener likeClickListener;

    public LikesFragmentModule(Context context, LikeClickListener likeClickListener) {
        // TODO: 21.11.2016 remove context
        this.likeClickListener = likeClickListener;
    }

    @Provides
    @FragmentScope
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    @FragmentScope
    GridLayoutManager provideGridLayoutManager(Context context) {
        return new GridLayoutManager(context, LikesFragment.GRID_VIEW_COLUMN_COUNT);
    }

    @Provides
    @FragmentScope
    LikesAdapter provideLikeAdapter() {
        return new LikesAdapter(likeClickListener);
    }
}
