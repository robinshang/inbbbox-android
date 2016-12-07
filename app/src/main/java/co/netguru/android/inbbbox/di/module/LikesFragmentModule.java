package co.netguru.android.inbbbox.di.module;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.view.ShotClickListener;
import co.netguru.android.inbbbox.feature.likes.adapter.LikesAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class LikesFragmentModule {

    private final ShotClickListener likeClickListener;

    public LikesFragmentModule(ShotClickListener likeClickListener) {
        this.likeClickListener = likeClickListener;
    }

    @Provides
    @FragmentScope
    LikesAdapter provideLikeAdapter() {
        return new LikesAdapter(likeClickListener);
    }
}
